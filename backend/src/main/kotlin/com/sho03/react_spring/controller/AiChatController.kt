package com.sho03.react_spring.controller

import com.sho03.react_spring.component.OpenAiClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/api/ai-chat")
class AiChatController(
    private val openAiClient: OpenAiClient,
) {

    @PostMapping
    fun chat(@RequestBody request: ChatRequestBody): ResponseEntity<String> {
        val result = openAiClient.chat(request.input)
        return ResponseEntity.ok(result)
    }

    data class ChatRequestBody(
        val input: String,
    )

    @PostMapping("/streaming")
    fun streamingChat(@RequestBody request: ChatRequestBody): Flux<String> {
        val result = openAiClient.streamingChat(request.input)
        return result
    }
}