package com.example.springwebfluxstudy.application.usecase

import com.example.springwebfluxstudy.adapter.`in`.web.dto.CurrencyConversionRequest
import com.example.springwebfluxstudy.adapter.`in`.web.dto.CurrencyConversionResponse
import com.example.springwebfluxstudy.domain.model.CurrencyExchangeLog
import com.example.springwebfluxstudy.domain.port.`in`.ConvertCurrency
import com.example.springwebfluxstudy.domain.port.out.CurrencyExchangeLogRepository
import com.example.springwebfluxstudy.domain.port.out.ExchangeRateClientPort
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ConvertCurrencyUsecase(
    private val exchangeRatePort: ExchangeRateClientPort,
    private val currencyExchangeLogRepository: CurrencyExchangeLogRepository
): ConvertCurrency {
    override fun convertCurrency(currency: CurrencyConversionRequest): Mono<CurrencyConversionResponse> {
        val exchangeRate = exchangeRatePort.getExchangeRate(currency.from, currency.to, currency.amount)
            .flatMap { exchangeRateResponse ->
                val exchangeLog = CurrencyExchangeLog(exchangeRateResponse)
                currencyExchangeLogRepository.save(exchangeLog)
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
}