package com.example.springwebfluxstudy.domain.port.out

import com.example.springwebfluxstudy.adapter.out.web.exchangeRate.dto.ExchangeRateConverterResponse
import reactor.core.publisher.Mono
import java.math.BigDecimal

interface ExchangeRateClientPort {
    fun getExchangeRate(from: String, to: String, amount: BigDecimal): Mono<ExchangeRateConverterResponse>
}