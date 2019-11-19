package systems.conduit.main.console;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.StringFormattedMessage;

public class MessageFactory implements org.apache.logging.log4j.message.MessageFactory {

    private static ColorReplacer colorReplacer = new ColorReplacer();

    @Override
    public Message newMessage(Object message) {
        return new StringFormattedMessage(colorReplacer.apply(message.toString()));
    }

    @Override
    public Message newMessage(String message) {
        return new StringFormattedMessage(colorReplacer.apply(message));
    }

    @Override
    public Message newMessage(String message, Object... params) {
        return new StringFormattedMessage(colorReplacer.apply(message), params);
    }
}
