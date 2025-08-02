package com.example.springwebfluxstudy.domain.model

import com.example.springwebfluxstudy.adapter.out.web.exchangeRate.dto.ExchangeRateConverterResponse
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Table("currency_exchange_log")
class CurrencyExchangeLog(

    @Id
    val id: Long? = null,

    @Column("external_id")
    val externalId: String,

    @Column("from_currency")
    val fromCurrency: String,

    @Column("to_currency")
    val toCurrency: String,

    val amount: BigDecimal,

    val quote: BigDecimal,

    @Column("converted_amount")
    val convertedAmount: BigDecimal,

    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
){
    constructor(exchangeRate: ExchangeRateConverterResponse) : this(
        externalId = UUID.randomUUID().toString(),
        fromCurrency = exchangeRate.query.from,
        toCurrency = exchangeRate.query.to,
        amount = exchangeRate.query.amount,
        quote = exchangeRate.info.quote,
        convertedAmount = exchangeRate.result,
    )
}