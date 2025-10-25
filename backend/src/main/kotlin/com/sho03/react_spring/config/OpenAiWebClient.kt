package com.sho03.react_spring.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.reactive.function.client.WebClient

@Component
class OpenAiClientConfiguration(
    private val openAIConfiguration: OpenAIConfiguration,
) {
    @Bean
    @Qualifier("openAiClient")
    fun openAiRestClient(builder: WebClient.Builder): RestClient {
        return RestClient.builder()
            .baseUrl(openAIConfiguration.baseUrl)
            .defaultHeaders { headers ->
                headers.setBearerAuth(openAIConfiguration.apiKey)
                headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            }
            .build()
    }

    @Bean
    @Qualifier("openAiStreamClient")
    fun openAiWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl(openAIConfiguration.baseUrl)
            .defaultHeaders { headers ->
                headers.setBearerAuth(openAIConfiguration.apiKey)
                headers.accept = listOf(MediaType.TEXT_EVENT_STREAM)
                headers.contentType = MediaType.APPLICATION_JSON
            }
            .build()

    }
}
