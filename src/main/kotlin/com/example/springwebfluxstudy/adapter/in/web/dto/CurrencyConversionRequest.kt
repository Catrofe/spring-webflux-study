package com.example.springwebfluxstudy.adapter.`in`.web.dto

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class CurrencyConversionRequest(
    @field:NotBlank
    val from: String,

    @field:NotBlank
    val to: String,

    @field:NotNull
    @field:DecimalMin(value = "1.0", inclusive = true)
    val amount: BigDecimal
)
