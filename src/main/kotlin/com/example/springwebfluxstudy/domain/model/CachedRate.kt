package com.example.springwebfluxstudy.domain.model

import java.math.BigDecimal

data class CachedRate(
    val quote: BigDecimal,
    val keyPrincipal: String
)
