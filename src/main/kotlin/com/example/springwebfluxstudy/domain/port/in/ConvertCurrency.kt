package com.example.springwebfluxstudy.domain.port.`in`

import com.example.springwebfluxstudy.adapter.`in`.web.dto.CurrencyConversionRequest
import com.example.springwebfluxstudy.adapter.`in`.web.dto.CurrencyConversionResponse
import reactor.core.publisher.Mono

interface ConvertCurrency {
    fun convertCurrency(currency : CurrencyConversionRequest): Mono<CurrencyConversionResponse>
}