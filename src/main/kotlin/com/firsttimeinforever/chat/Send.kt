package com.firsttimeinforever.chat

import com.rabbitmq.client.ConnectionFactory
import java.nio.charset.StandardCharsets


object Send {
    private const val QUEUE_NAME = "hello"

    @Throws(Exception::class)
    @JvmStatic
    fun main(argv: Array<String>) {
        val factory = ConnectionFactory()
        factory.host = "localhost"
        factory.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                channel.queueDeclare(QUEUE_NAME, false, false, false, null)
                val message = "Hello World!"
                channel.basicPublish(
                    "",
                    QUEUE_NAME,
                    null,
                    message.toByteArray(StandardCharsets.UTF_8)
                )
                println(" [x] Sent '$message'")
            }
        }
    }
}