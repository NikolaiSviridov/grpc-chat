package com.firsttimeinforever.chat

import com.rabbitmq.client.ConnectionFactory
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.collections.HashMap

class ChatClient(private var UserName: String) {
    private val hashRecv: HashMap<String, Recv> = HashMap()
    fun start() {
        println("Ready for messages")
        val input = Scanner(System.`in`)
        while (input.hasNextLine()) {
            val line = input.nextLine()
            if (line.isBlank()) {
                continue
            }

            val values = line.split(" ")
            if (values.size > 1) {
                val cmd = values[0]

                when (cmd) {
                    "user" -> UserName = values[1]
                    "c" -> addChannel(values[1])
                    "w" -> send(UserName, values[1], values.subList(2, values.size).joinToString(separator = " "))
                    "dc" -> deleteChannel(values[1])
                    else -> println("Smth wrong")
                }
            }

            if (line == "exit") {
                break
            }
        }
    }
    private fun addChannel(channelName : String ) {
        if (hashRecv.containsKey(channelName)) {
            println("already go channel '$channelName'")
            return
        }

        val recv = Recv(channelName)
        recv.start()
        hashRecv.put(channelName, recv)
        println("Connected to '$channelName'")
    }

    private fun send(user:String, channelName:String, msg:String) {
        if (!hashRecv.containsKey(channelName)) {
            println("Doesn't have channel '$channelName'")
            return
        }

        val factory = ConnectionFactory()
        factory.host = "localhost"
        factory.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                channel.basicPublish(
                    channelName,
                    "",
                    null,
                    ("Channel $channelName| $user: $msg").toByteArray(StandardCharsets.UTF_8)
                )
            }
        }
    }

    private fun deleteChannel(channelName : String) {
        if (!hashRecv.containsKey(channelName)) {
            println("Doesn't have channel '$channelName'")
            return
        }

        hashRecv.get(channelName)?.stop()
        hashRecv.remove(channelName)
        println("Left '$channelName'")
    }
}
