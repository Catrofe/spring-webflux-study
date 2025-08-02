package com.example.springwebfluxstudy.adapter.out.web.exchangeRate.dto

import java.math.BigDecimal

data class ExchangeRateConverterResponse(
    val success: Boolean,
    val terms: String,
    val privacy: String,
    val query: ExchangeRateConverterQuery,
    val info: ExchangeRateConverterInfo,
    val result: BigDecimal
)
