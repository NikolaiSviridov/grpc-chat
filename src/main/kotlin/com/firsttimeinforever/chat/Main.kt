package com.firsttimeinforever.chat

import java.awt.Point
import java.awt.Toolkit


fun main(args: Array<String>) {
    val frame = ChatClient(args[0])
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val midle = Point(screenSize.width/2, screenSize.height/2)
    when (args[1]) {
        "0" -> frame.setLocation(Point(0, 0))
        "1" -> frame.setLocation(Point(midle.x, 0))
        "2" -> frame.setLocation(Point(0, midle.y))
        "3" -> frame.setLocation(Point(midle.x, midle.y))
    }
    frame.isVisible = true
}
