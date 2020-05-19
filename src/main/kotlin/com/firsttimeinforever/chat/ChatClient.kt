package com.firsttimeinforever.chat

import com.firsttimeinforever.chat.ChatServiceOuterClass.ChatMessage
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import java.util.*

class ChatClient(private val properties: ClientProperties) {
    private val channel = ManagedChannelBuilder.forAddress(properties.connection.hostname, properties.connection.port).usePlaintext().build()
    private val chatService = ChatServiceGrpc.newStub(channel);
    private val chat = chatService.chat(object: StreamObserver<ChatMessageFromServer?> {
        override fun onNext(receivedMessage: ChatMessageFromServer?) {
            if (receivedMessage == null) {
                return
            }
            println("${Date(receivedMessage.timestamp.seconds)}: ${receivedMessage.message.from} wrote: ${receivedMessage.message.text}")
        }

        override fun onError(t: Throwable) {
            t.printStackTrace()
            println("Disconnected")
        }

        override fun onCompleted() {
            println("Disconnected")
        }
    })

    fun start() {
        println("Ready for messages")
        val input = Scanner(System.`in`)
        while (input.hasNextLine()) {
            val line = input.nextLine()
            if (line.isBlank()) {
                continue
            }
            if (line == "exit") {
                chat.onCompleted()
                channel.shutdown()
                break
            }
            chat.onNext(ChatMessage.newBuilder().setFrom(properties.userName).setText(line).build())
        }
    }
}
