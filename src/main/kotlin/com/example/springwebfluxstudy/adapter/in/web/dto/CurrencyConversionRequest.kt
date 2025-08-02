package com.example.springwebfluxstudy.adapter.`in`.web.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class CurrencyConversionRequest(
    @field:NotBlank
    val from: String,

    @field:NotBlank
    val to: String,

    @field:NotNull
    val amount: BigDecimal
)
