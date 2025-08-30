package com.sho03.react_spring.component

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.sho03.react_spring.config.OpenAIConfiguration
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class OpenAiClient(
    private val openAIConfiguration: OpenAIConfiguration,
    @Qualifier("openAiClient") private val openAiClient: RestClient,
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
}
