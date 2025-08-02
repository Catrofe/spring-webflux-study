package com.example.springwebfluxstudy.adapter.out.web.exchangeRate.dto

import java.math.BigDecimal

data class ExchangeRateConverterInfo(
    val timestamp: Long,
    val quote: BigDecimal,
)
