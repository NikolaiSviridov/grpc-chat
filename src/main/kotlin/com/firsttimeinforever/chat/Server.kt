package com.firsttimeinforever.chat

import io.grpc.ServerBuilder

class Server(private val settings: ServerProperties) {
    private val server = ServerBuilder.forPort(settings.port).addService(ChatServiceImpl()).build()

    fun start() {
        server.start()
        server.awaitTermination()
    }

    fun stop() {
        server.shutdown()
    }
}
