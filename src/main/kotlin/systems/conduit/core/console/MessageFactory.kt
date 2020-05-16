package systems.conduit.core.console

import org.apache.logging.log4j.message.Message
import org.apache.logging.log4j.message.MessageFactory
import org.apache.logging.log4j.message.StringFormattedMessage

class MessageFactory: MessageFactory {

    override fun newMessage(message: Any): Message {
        return StringFormattedMessage(colorReplacer.apply(message.toString()))
    }

    override fun newMessage(message: String): Message {
        return StringFormattedMessage(colorReplacer.apply(message))
    }

    override fun newMessage(message: String, vararg params: Any): Message {
        return StringFormattedMessage(colorReplacer.apply(message), *params)
    }

    companion object {
        private val colorReplacer = ConsoleColorUtil()
    }
}
