package com.example.springwebfluxstudy.adapter.out.web.exchangeRate

import com.example.springwebfluxstudy.adapter.out.web.exchangeRate.dto.ExchangeRateConverterResponse
import com.example.springwebfluxstudy.domain.port.out.ExchangeRateClientPort
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.math.BigDecimal

@Service
class ExchangeRateClientAdapter(
    private val webClient: WebClient,

    @Value("\${exchange.rate.api.access.key}")
    private val accessKey: String,

    @Value("\${exchange.rate.api.url}")
    private val baseUrl: String

): ExchangeRateClientPort {

    private val logger = LoggerFactory.getLogger(ExchangeRateClientAdapter::class.java)

    override fun getExchangeRate(from: String, to: String, amount: BigDecimal): Mono<ExchangeRateConverterResponse> {
        logger.info("getExchangeRate from: $from, to: $to, amount: $amount")
        val url = "$baseUrl/convert?access_key=$accessKey&from=$from&to=$to&amount=$amount"
        return webClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(ExchangeRateConverterResponse::class.java)
            .onErrorResume {
                Mono.error(RuntimeException("Error integrating with partner"))
            }
    }

}