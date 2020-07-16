package com.yusys.log4j2.extender.logger;

import org.apache.logging.log4j.message.AbstractMessageFactory;
import org.apache.logging.log4j.message.Message;

/**
 * 自定义日志消息工厂
 */
public class DesignedParameterizedMessageFactory extends AbstractMessageFactory {
    /**
     * Instance of ParameterizedMessageFactory.
     */
    public static final DesignedParameterizedMessageFactory INSTANCE = new DesignedParameterizedMessageFactory();

    private static final long serialVersionUID = -8970940216592525651L;

    /**
     * Constructs a message factory.
     */
    public DesignedParameterizedMessageFactory() {
        super();
    }

    @Override
    public Message newMessage(CharSequence message) {
        return new DesignedSimpleMessage(message);
    }

    @Override
    public Message newMessage(Object message) {
        return new DesignedObjectMessage(message);
    }

    @Override
    public Message newMessage(String message) {
        return new DesignedSimpleMessage(message);
    }

    /**
     * Creates {@link DesignedParameterizedMessage} instances.
     *
     * @param message The message pattern.
     * @param params The message parameters.
     * @return The Message.
     *
     * @see DesignedParameterizedMessage#(String, Object...)
     */
    @Override
    public Message newMessage(final String message, final Object... params) {
        return new DesignedParameterizedMessage(message, params);
    }

    /**
     * @since 2.6.1
     */
    @Override
    public Message newMessage(final String message, final Object p0) {
        return new DesignedParameterizedMessage(message, p0);
    }

    /**
     * @since 2.6.1
     */
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1) {
        return new DesignedParameterizedMessage(message, p0, p1);
    }

    /**
     * @since 2.6.1
     */
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2) {
        return new DesignedParameterizedMessage(message, p0, p1, p2);
    }

    /**
     * @since 2.6.1
     */
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        return new DesignedParameterizedMessage(message, p0, p1, p2, p3);
    }

    /**
     * @since 2.6.1
     */
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        return new DesignedParameterizedMessage(message, p0, p1, p2, p3, p4);
    }

    /**
     * @since 2.6.1
     */
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        return new DesignedParameterizedMessage(message, p0, p1, p2, p3, p4, p5);
    }

    /**
     * @since 2.6.1
     */
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
                              final Object p6) {
        return new DesignedParameterizedMessage(message, p0, p1, p2, p3, p4, p5, p6);
    }

    /**
     * @since 2.6.1
     */
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
                              final Object p6, final Object p7) {
        return new DesignedParameterizedMessage(message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    /**
     * @since 2.6.1
     */
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
                              final Object p6, final Object p7, final Object p8) {
        return new DesignedParameterizedMessage(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    /**
     * @since 2.6.1
     */
    @Override
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5,
                              final Object p6, final Object p7, final Object p8, final Object p9) {
        return new DesignedParameterizedMessage(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }
}
