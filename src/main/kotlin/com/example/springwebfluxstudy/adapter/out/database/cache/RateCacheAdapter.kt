package com.example.springwebfluxstudy.adapter.out.database.cache


import com.example.springwebfluxstudy.domain.model.CachedRate
import com.example.springwebfluxstudy.domain.model.CurrencyExchangeLog
import com.example.springwebfluxstudy.domain.port.out.RateCachePort
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlin.time.toJavaDuration


@Service
class RateCacheAdapter(
    private val redisTemplate: ReactiveRedisTemplate<String, CachedRate>
): RateCachePort {

    private val logger = LoggerFactory.getLogger(RateCacheAdapter::class.java)

    private val duration: Duration = 30.toDuration(DurationUnit.MINUTES)

    override fun getRate(from: String, to: String): Mono<CachedRate> {
        val key = generateCacheKey(from, to)
        return redisTemplate.opsForValue()
            .get(key)
            .switchIfEmpty(Mono.error(RuntimeException("Rate not found for key: $key")))
    }

    override fun saveRate(rate: CurrencyExchangeLog): Mono<Void> {
        val key = generateCacheKey(rate.fromCurrency, rate.toCurrency)
        val cachedRate = CachedRate(rate.quote, rate.fromCurrency)
        return redisTemplate.opsForValue()
            .set(key, cachedRate, duration.toJavaDuration())
            .then()
            .doOnSuccess {
                logger.info("Rate cache saved: $key -> $cachedRate")
            }
            .doOnError { error ->
                logger.info("Failed to save rate cache: $key -> $cachedRate, error: ${error.message}")
                throw RuntimeException("Failed to save rate: ${error.message}")
            }
    }

    fun generateCacheKey(from: String, to: String): String {
        return if (from < to) "$from:$to" else "$to:$from"
    }
}