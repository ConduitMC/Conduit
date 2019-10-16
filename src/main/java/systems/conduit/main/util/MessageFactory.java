package systems.conduit.main.util;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.StringFormattedMessage;

public class MessageFactory implements org.apache.logging.log4j.message.MessageFactory {

    @Override
    public Message newMessage(Object message) {
        return new StringFormattedMessage(ColorReplacer.getColoredMessage(message.toString()));
    }

    @Override
    public Message newMessage(String message) {
        return new StringFormattedMessage(ColorReplacer.getColoredMessage(message));
    }

    @Override
    public Message newMessage(String message, Object... params) {
        return new StringFormattedMessage(ColorReplacer.getColoredMessage(message), params);
    }
}
