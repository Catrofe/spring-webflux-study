package com.example.springwebfluxstudy.adapter.`in`.web.dto

import com.example.springwebfluxstudy.domain.model.CurrencyExchangeLog
import java.math.BigDecimal
import java.time.LocalDateTime

data class CurrencyConversionResponse(
    val externalId: String,
    val from: String,
    val to: String,
    val amount: BigDecimal,
    val quote: BigDecimal,
    val convertedAmount: BigDecimal,
    val createdAt: LocalDateTime
){
    constructor(currency: CurrencyExchangeLog) : this(
        externalId = currency.externalId,
        from = currency.fromCurrency,
        to = currency.toCurrency,
        amount = currency.amount,
        quote = currency.quote,
        convertedAmount = currency.convertedAmount,
        createdAt = currency.createdAt
    )
}
