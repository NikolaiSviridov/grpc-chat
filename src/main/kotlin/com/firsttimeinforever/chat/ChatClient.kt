package com.firsttimeinforever.chat

import com.rabbitmq.client.ConnectionFactory
import java.awt.*
import java.nio.charset.StandardCharsets
import javax.swing.*


class ChatClient(private var UserName: String) : JFrame() {
    private val hashRecv: HashMap<String, Recv> = HashMap()
    private val textChannel: HashMap<String, JTextArea> = HashMap()
    val connectToChannelButton = JButton("Connect")
    val channelLine = JTextField()
    val msgButton = JButton("Send")
    val msgLine = JTextField()
    val tabbedPane = JTabbedPane()

    init {
        layout = BorderLayout()
        createUI("gucci Chat")
        connectButtons()
    }

    private fun createUI(title: String) {

        setTitle(title)

        val panelChannel = JPanel()
        val label = JLabel(UserName)
        label.setSize(20, 50)
        panelChannel.add(label, 0,0)
        panelChannel.add(channelLine,1,0)
        panelChannel.add(connectToChannelButton,1,1)
        panelChannel.layout = GridLayout(2,2)
        add(panelChannel, BorderLayout.NORTH)

        val panelMsg = JPanel()
        panelMsg.add(msgLine)
        panelMsg.add(msgButton)
        panelMsg.layout = GridLayout()
        add(panelMsg, BorderLayout.SOUTH)

        add(tabbedPane, BorderLayout.CENTER)

        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setSize(600, 400)
        setLocationRelativeTo(null)
    }

    private fun connectButtons() {
        connectToChannelButton.addActionListener {
            val value = channelLine.text
            val area = JTextArea()
            area.isEditable = false
            area.font = area.font.deriveFont(16)
            textChannel.put(value, area)
            tabbedPane.add("$value", area)
            addChannel(value)
            channelLine.text = ""
        }

        msgButton.addActionListener {
            val value = msgLine.text
            val idx = tabbedPane.selectedIndex
            val channel = tabbedPane.getTitleAt(idx)
            send(UserName, channel, value)
            msgLine.text = ""
        }
    }

    private fun addChannel(channelName: String) {
        if (hashRecv.containsKey(channelName)) {
            println("already got channel '$channelName'")
            return
        }

        createChannel(channelName)
        val recv = Recv(channelName)
        textChannel.get(channelName)?.let { recv.start(it) }
        hashRecv.put(channelName, recv)
        println("Connected to '$channelName'")
    }

    private fun send(user: String, channelName: String, msg: String) {
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

    private fun deleteChannel(channelName: String) {
        if (!hashRecv.containsKey(channelName)) {
            println("Doesn't have channel '$channelName'")
            return
        }

        hashRecv.get(channelName)?.stop()
        hashRecv.remove(channelName)
        println("Left '$channelName'")
    }

    private fun createChannel(channelName: String) {
        val factory = ConnectionFactory()
        factory.host = "localhost"
        val connection = factory.newConnection()
        val channel = connection.createChannel()
        channel.exchangeDeclare(channelName, "fanout")
        println("Created channel '$channelName'")
    }
}
