package com.firsttimeinforever.chat

import java.awt.*

var USER = "user"
fun createAndShowGUI() {
    val frame = ChatClient(USER)
    frame.isVisible = true
}

fun main(args: Array<String>) {
    USER = args[0]
    EventQueue.invokeLater(::createAndShowGUI)
}
