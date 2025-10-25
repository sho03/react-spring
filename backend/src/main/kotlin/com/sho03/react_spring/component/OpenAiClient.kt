package com.sho03.react_spring.component

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.sho03.react_spring.config.OpenAIConfiguration
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@Component
class OpenAiClient(
    private val openAIConfiguration: OpenAIConfiguration,
    @Qualifier("openAiClient") private val openAiClient: RestClient,
    @Qualifier("openAiStreamClient") private val openAiStreamClient: WebClient,
    private val mapper: ObjectMapper,
) {
    fun chat(input: String): String {
        val requestBody = mapOf(
            "model" to openAIConfiguration.model,
            "input" to input
        )
        val rawJson = openAiClient
            .post()
            .uri("/responses")
            .body(requestBody)
            .retrieve()
            .body(String::class.java)

        if (rawJson == null) {
            throw IllegalStateException()
        }

        val response: Response = mapper
            .readerFor(Response::class.java)
            .readValue(rawJson)

        return response.output.first().content.first().text
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Response(
        val output: List<Output>
    )

    data class Output(
        val type: String,
        val id: String,
        val status: String,
        val role: String,
        val content: List<Content>
    )

    data class Content(
        val type: String,
        val text: String,
    )

    fun streamingChat(input: String): Flux<String> {
        val requestBody = mapOf(
            "model" to openAIConfiguration.model,
            "input" to input,
            "stream" to true
        )
        val flux = openAiStreamClient
            .post()
            .uri("/responses")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.TEXT_EVENT_STREAM)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToFlux(String::class.java)
            .map { json ->
                val nodes = ObjectMapper().readTree(json)
                val output = nodes["delta"]
                if (output != null) {
                    output.asText()
                } else {
                    ""
                }
            }

        return flux
    }
}
