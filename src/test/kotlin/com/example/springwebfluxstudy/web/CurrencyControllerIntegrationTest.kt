package com.example.springwebfluxstudy.web

import com.example.springwebfluxstudy.adapter.`in`.web.dto.CurrencyConversionRequest
import com.example.springwebfluxstudy.adapter.`in`.web.dto.CurrencyConversionResponse
import com.example.springwebfluxstudy.adapter.out.web.exchangeRate.dto.ExchangeRateConverterResponse
import com.example.springwebfluxstudy.domain.model.CachedRate
import com.example.springwebfluxstudy.domain.model.CurrencyExchangeLog
import com.example.springwebfluxstudy.domain.port.out.CurrencyExchangeLogRepository
import com.example.springwebfluxstudy.domain.port.out.ExchangeRateClientPort
import com.example.springwebfluxstudy.domain.port.out.RateCachePort
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import java.math.BigDecimal
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class CurrencyControllerIntegrationTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockitoBean
    private lateinit var exchangeRateClientPort: ExchangeRateClientPort

    @MockitoBean
    private lateinit var currencyExchangeLogRepository: CurrencyExchangeLogRepository

    @MockitoBean
    private lateinit var rateCachePort: RateCachePort

    @Test
    fun `deve converter moeda com sucesso`() {
        val request = CurrencyConversionRequest(
            from = "USD",
            to = "BRL",
            amount = BigDecimal("100.00")
        )

        val mockedResponse = ExchangeRateConverterResponse(
            currency = request,
            quote = BigDecimal("5.50"),
            result = BigDecimal("550.00")
        )

        given(exchangeRateClientPort.getExchangeRate(any(), any(), any()))
            .willReturn(Mono.just(mockedResponse))

        given(rateCachePort.getRate(any(), any()))
            .willReturn(Mono.empty())

        given(currencyExchangeLogRepository.save(any()))
            .willAnswer { invocation ->
                Mono.just(invocation.getArgument(0, CurrencyExchangeLog::class.java))
            }

        given(rateCachePort.saveRate(any()))
            .willReturn(Mono.empty())

        webTestClient.post()
            .uri("/v1/currency")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectBody(CurrencyConversionResponse::class.java)
            .consumeWith { response ->
                val body = response.responseBody
                assert(body != null)
                assertEquals(0, body!!.convertedAmount.compareTo(BigDecimal("550.00")))
            }
    }


    @Test
    fun `deve encontrar moeda no cache com sucesso`() {
        val request = CurrencyConversionRequest(
            from = "USD",
            to = "BRL",
            amount = BigDecimal("200.00")
        )

        val cachedRate = CachedRate(
            quote = BigDecimal("5.50"),
            keyPrincipal = "USD"
        )

        given(rateCachePort.getRate(any(), any()))
            .willReturn(Mono.just(cachedRate))

        given(exchangeRateClientPort.getExchangeRate(any(), any(), any()))
            .willReturn(Mono.empty())

        given(currencyExchangeLogRepository.save(any()))
            .willAnswer { invocation ->
                Mono.just(invocation.getArgument(0, CurrencyExchangeLog::class.java))
            }

        webTestClient.post()
            .uri("/v1/currency")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectBody(CurrencyConversionResponse::class.java)
            .consumeWith { response ->
                val body = response.responseBody
                assert(body != null)
                assertEquals(0, body!!.convertedAmount.compareTo(BigDecimal("1100.00")))
            }
    }

    @Test
    fun `deve encontrar moeda no cache invertida com sucesso`() {
        val request = CurrencyConversionRequest(
            from = "USD",
            to = "BRL",
            amount = BigDecimal("200.00")
        )

        val cachedRate = CachedRate(
            quote = BigDecimal("0.20"),
            keyPrincipal = "BRL"
        )

        given(rateCachePort.getRate(any(), any()))
            .willReturn(Mono.just(cachedRate))

        given(exchangeRateClientPort.getExchangeRate(any(), any(), any()))
            .willReturn(Mono.empty())

        given(currencyExchangeLogRepository.save(any()))
            .willAnswer { invocation ->
                Mono.just(invocation.getArgument(0, CurrencyExchangeLog::class.java))
            }

        webTestClient.post()
            .uri("/v1/currency")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectBody(CurrencyConversionResponse::class.java)
            .consumeWith { response ->
                val body = response.responseBody
                assert(body != null)
                assertEquals(0, body!!.convertedAmount.compareTo(BigDecimal("1000.00")))
            }
    }
}
