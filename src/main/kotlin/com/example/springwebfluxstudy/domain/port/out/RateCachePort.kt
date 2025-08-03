package com.example.springwebfluxstudy.domain.port.out

import com.example.springwebfluxstudy.domain.model.CachedRate
import com.example.springwebfluxstudy.domain.model.CurrencyExchangeLog
import reactor.core.publisher.Mono

interface RateCachePort {
    fun getRate(from: String, to: String): Mono<CachedRate>

    fun saveRate(rate: CurrencyExchangeLog): Mono<Void>
}