package com.sho03.react_spring.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties

@ConfigurationProperties(prefix = "openai")
data class OpenAIConfiguration(
    var apiKey: String,
    var baseUrl: String = "https://api.openai.com/v1",
    var model: String = "gpt-4o-mini"
)

