package com.firsttimeinforever.chat

import com.rabbitmq.client.ConnectionFactory
import java.util.*

fun main(args: Array<String>) {
    val factory = ConnectionFactory()
    factory.host = "localhost"
    val connection = factory.newConnection()
    val channel = connection.createChannel()
    val exchangeList = Arrays.asList("c1", "c2","c3")
    for (exchange in exchangeList) {
        channel.exchangeDeclare(exchange, "fanout")
    }
    val chat = ChatClient(args[0])
    chat.start()

}
