package com.firsttimeinforever.chat

fun main(args: Array<String>) {
    when (args.size) {
        2 -> {
            val port = Integer.parseInt(args.first())
            val userName = args[1]
            val server = Server(ServerProperties(port))
            Thread(Runnable {
                server.start()
            }).start()
            val client = ChatClient(ClientProperties(ClientConnectionProperties(port, "localhost"), userName))
            client.start()
            server.stop()
        }
        3 -> {
            val hostname = args.first()
            val port = Integer.parseInt(args[1])
            val userName = args[2]
            val client = ChatClient(ClientProperties(ClientConnectionProperties(port, hostname), userName))
            client.start()
        }
        else -> {
            println("Bad arguments")
        }
    }
}
