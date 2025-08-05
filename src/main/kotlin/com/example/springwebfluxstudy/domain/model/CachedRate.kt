package com.example.springwebfluxstudy.domain.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class CachedRate @JsonCreator constructor(
    @JsonProperty("quote") val quote: BigDecimal,
    @JsonProperty("keyPrincipal") val keyPrincipal: String
)
