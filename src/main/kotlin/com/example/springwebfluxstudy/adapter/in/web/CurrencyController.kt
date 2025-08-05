package com.example.springwebfluxstudy.adapter.`in`.web

import com.example.springwebfluxstudy.adapter.`in`.web.dto.CurrencyConversionRequest
import com.example.springwebfluxstudy.adapter.`in`.web.dto.CurrencyConversionResponse
import com.example.springwebfluxstudy.domain.port.`in`.ConvertCurrency
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/v1/currency")
class CurrencyController(
    private val convertCurrency: ConvertCurrency
) {

    @PostMapping
    fun exchangeCurrency(
        @RequestBody @Valid currencyConversionRequest: CurrencyConversionRequest,
    ): Mono<CurrencyConversionResponse> {
        return convertCurrency.convertCurrency(currencyConversionRequest)
    }
}