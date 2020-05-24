package com.firsttimeinforever.chat

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery


class Recv(private val EXCHANGE: String) {

    fun start() {
        val factory = ConnectionFactory()
        factory.host = "localhost"
        val connection = factory.newConnection()
        val channel = connection.createChannel()
        channel.exchangeDeclare(EXCHANGE, "fanout")
        val QUEUE_NAME = channel.queueDeclare().queue
        channel.queueBind(QUEUE_NAME, EXCHANGE, "")
//    println(" [*] Waiting for messages. To exit press CTRL+C")
        val deliverCallback = DeliverCallback { consumerTag: String?, delivery: Delivery ->
            val message = String(delivery.body)
            println(" [x] '$message'")
        }
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, CancelCallback { consumerTag: String? -> })


    }

}