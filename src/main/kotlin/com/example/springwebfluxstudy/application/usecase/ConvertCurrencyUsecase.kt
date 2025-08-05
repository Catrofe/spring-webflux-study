package com.example.springwebfluxstudy.application.usecase

import com.example.springwebfluxstudy.adapter.`in`.web.dto.CurrencyConversionRequest
import com.example.springwebfluxstudy.adapter.`in`.web.dto.CurrencyConversionResponse
import com.example.springwebfluxstudy.adapter.out.web.exchangeRate.dto.ExchangeRateConverterResponse
import com.example.springwebfluxstudy.domain.model.CurrencyExchangeLog
import com.example.springwebfluxstudy.domain.port.`in`.ConvertCurrency
import com.example.springwebfluxstudy.domain.port.out.CurrencyExchangeLogRepository
import com.example.springwebfluxstudy.domain.port.out.ExchangeRateClientPort
import com.example.springwebfluxstudy.domain.port.out.RateCachePort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.math.MathContext

@Service
class ConvertCurrencyUsecase(
    private val exchangeRatePort: ExchangeRateClientPort,
    private val currencyExchangeLogRepository: CurrencyExchangeLogRepository,
    private val rateCachePort: RateCachePort
): ConvertCurrency {

    private val logger = LoggerFactory.getLogger(ConvertCurrencyUsecase::class.java)


    override fun convertCurrency(currency: CurrencyConversionRequest): Mono<CurrencyConversionResponse> {
        logger.info("convertCurrency: $currency")
        val exchangeRate = getExchangeRate(currency)
            .flatMap { exchangeRateResponse ->
                val exchangeLog = CurrencyExchangeLog(exchangeRateResponse)
                currencyExchangeLogRepository.save(exchangeLog)
                    .flatMap {
                        if (!exchangeRateResponse.cache) {
                            rateCachePort.saveRate(exchangeLog)
                        } else {
                            Mono.empty()
                        }
                    }
                    .thenReturn(exchangeLog)
            }
            .map { currencyExchangeLog ->
                CurrencyConversionResponse(
                    currencyExchangeLog
                )
            }

        return exchangeRate.onErrorResume {
            Mono.error(
                RuntimeException("The submitted amount could not be converted. Please try again later.")
            )
        }
    }

    private fun getExchangeRate(currency: CurrencyConversionRequest): Mono<ExchangeRateConverterResponse> {
        return currencyCache(currency)
            .switchIfEmpty(
                exchangeRatePort.getExchangeRate(currency.from, currency.to, currency.amount)
            )
    }

    private fun currencyCache(currency: CurrencyConversionRequest): Mono<ExchangeRateConverterResponse> {
        val cachedRate = rateCachePort.getRate(currency.from, currency.to)
            .flatMap { cachedRate ->
                if (cachedRate.keyPrincipal == currency.from) {
                    Mono.just(
                        ExchangeRateConverterResponse(
                            currency,
                            cachedRate.quote,
                            cachedRate.quote * currency.amount
                        )
                    )
                } else {
                    val reversedQuote = calculateReversedQuote(cachedRate.quote)
                    Mono.just(
                        ExchangeRateConverterResponse(
                            currency.copy(from = currency.to, to = currency.from),
                            reversedQuote,
                            (currency.amount * reversedQuote)
                        )
                    )
                }
            }
        return cachedRate.onErrorResume {
            Mono.empty()
        }
    }

    private fun calculateReversedQuote(quote: BigDecimal): BigDecimal =
        BigDecimal.ONE.divide(quote, MathContext.DECIMAL128)
            .setScale(6, java.math.RoundingMode.HALF_UP)
}


