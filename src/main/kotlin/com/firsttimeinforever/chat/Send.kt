package com.firsttimeinforever.chat

import com.rabbitmq.client.ConnectionFactory
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
            val line = input.nextLine()
            val values = line.split(" ")
            val exchange = values[0]
            val message = values[1]
            factory.newConnection().use { connection ->
                connection.createChannel().use { channel ->
                    channel.basicPublish(
                        exchange,
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