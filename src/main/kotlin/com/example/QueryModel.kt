package com.example

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.engine.cio.*
import io.ktor.http.*

suspend fun queryModel(prompt: String): String {
    val client = HttpClient(CIO) {
        engine {
            requestTimeout = 100_000 // 100 seconds
        }
    }
    return try {
        val response: HttpResponse = client.post("http://localhost:11434/api/generate") {
            contentType(ContentType.Application.Json)
            setBody("""{ "model": "llama3.2", "prompt": "$prompt" }""")
        }
        response.bodyAsText()
    } catch (e: Exception) {
        "Error querying the model: ${e.message}"
    } finally {
        client.close()
    }
}

suspend fun main() {
    val prompt = "What is the meaning of Nnaemeka?"
    val response = queryModel(prompt)
    println("Model Response: $response")
}
