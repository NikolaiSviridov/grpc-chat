package com.firsttimeinforever.chat

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.MessageProperties
import java.nio.charset.StandardCharsets
import java.util.*

class Send(
    private val EXCHANGE: String,
    private val USER: String
) {

    fun start() {
        val factory = ConnectionFactory()
        factory.host = "localhost"
        val input = Scanner(System.`in`)
        while (input.hasNextLine()) {
            val message = input.nextLine()

            factory.newConnection().use { connection ->
                connection.createChannel().use { channel ->
                    channel.exchangeDeclare(EXCHANGE, "fanout")
                    channel.basicPublish(
                        EXCHANGE,
                        "",
                        null,
                        ("$USER: $message").toByteArray(StandardCharsets.UTF_8)
                    )
//                    println(" [x] '$message'")
                }
            }
        }
    }


}