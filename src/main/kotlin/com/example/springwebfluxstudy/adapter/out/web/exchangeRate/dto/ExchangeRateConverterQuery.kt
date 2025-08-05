package com.example.springwebfluxstudy.adapter.out.web.exchangeRate.dto

import java.math.BigDecimal

data class ExchangeRateConverterQuery(
    val from: String,
    val to: String,
    val amount: BigDecimal,
)
