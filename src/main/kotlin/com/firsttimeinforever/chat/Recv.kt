package com.firsttimeinforever.chat

import com.rabbitmq.client.*
import javax.swing.JTextArea


class Recv(private val exchange: String) {

    var QUEUE_NAME: String? = null
    var channel: Channel? = null
    fun start(area: JTextArea) {
        val factory = ConnectionFactory()
        factory.host = "localhost"
        val connection = factory.newConnection()
        channel = connection.createChannel()
        QUEUE_NAME = channel?.queueDeclare()?.queue
        channel?.queueBind(QUEUE_NAME, exchange, "")
        val deliverCallback = DeliverCallback { consumerTag: String?, delivery: Delivery ->
            val message = String(delivery.body)
            println(" [x] '$message'")
            area.append(message + "\n")
        }
        channel?.basicConsume(QUEUE_NAME, true, deliverCallback, CancelCallback { consumerTag: String? -> })

    }

    fun stop() {
        channel?.abort()
    }
}