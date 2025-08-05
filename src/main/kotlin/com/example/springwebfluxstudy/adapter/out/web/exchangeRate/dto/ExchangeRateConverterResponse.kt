package com.example.springwebfluxstudy.adapter.out.web.exchangeRate.dto

import com.example.springwebfluxstudy.adapter.`in`.web.dto.CurrencyConversionRequest
import java.math.BigDecimal

data class ExchangeRateConverterResponse(
    val success: Boolean,
    val query: ExchangeRateConverterQuery,
    val info: ExchangeRateConverterInfo,
    val result: BigDecimal,
    val cache: Boolean = false
){
    constructor(currency: CurrencyConversionRequest, quote: BigDecimal, result: BigDecimal) : this(
        success = true,
        query = ExchangeRateConverterQuery(
            from = currency.from,
            to = currency.to,
            amount = currency.amount
        ),
        info = ExchangeRateConverterInfo(
            timestamp = System.currentTimeMillis() / 1000,
            quote = quote,
        ),
        result = result,
        cache = true
    )
}
