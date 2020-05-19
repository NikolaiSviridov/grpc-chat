package com.firsttimeinforever.chat

import com.google.protobuf.Timestamp
import io.grpc.stub.StreamObserver
import java.util.*
import java.util.concurrent.ConcurrentHashMap

typealias ChatMessageFromServer = ChatServiceOuterClass.ChatMessageFromServer
typealias ChatMessage = ChatServiceOuterClass.ChatMessage

class ChatServiceImpl : ChatServiceGrpc.ChatServiceImplBase() {
    companion object {
        val observers = ConcurrentHashMap.newKeySet<StreamObserver<ChatMessageFromServer>>()
    }

    override fun chat(responseObserver: StreamObserver<ChatMessageFromServer>?): StreamObserver<ChatMessage> {
        observers.add(responseObserver)
        return object : StreamObserver<ChatMessage> {
            override fun onNext(message: ChatMessage?) {
                val message = ChatMessageFromServer.newBuilder()
                    .setMessage(message)
                    .setTimestamp(Timestamp.newBuilder().setSeconds(Date().time).setNanos(0).build()).build()
                for (observer in observers) {
                    observer.onNext(message)
                }
            }

            override fun onError(t: Throwable?) {
                t?.printStackTrace()
                println("Disconnected")
            }

            override fun onCompleted() {
                observers.remove(responseObserver)
            }
        }
    }
}
