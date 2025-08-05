package com.example.springwebfluxstudy.domain.port.out

import com.example.springwebfluxstudy.domain.model.CurrencyExchangeLog
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface CurrencyExchangeLogRepository: ReactiveCrudRepository<CurrencyExchangeLog, Long> {
}