package com.firsttimeinforever.chat

fun main(args: Array<String>) {

    when (args.size) {
        2 -> {
            val recv = Recv(args[0])
            Thread(Runnable {
                recv.start()
            }).start()
            val sender = Send(args[0], args[1])
            sender.start()
        }
        else -> {
            println("Bad arguments")
        }
    }
}
