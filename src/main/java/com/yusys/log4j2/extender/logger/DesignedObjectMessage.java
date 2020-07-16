package com.yusys.log4j2.extender.logger;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.StringBuilderFormattable;
import org.apache.logging.log4j.util.StringBuilders;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 重写formatTo方法，增加自定义特殊处理
 */
public class DesignedObjectMessage implements Message, StringBuilderFormattable {
    private static final long serialVersionUID = -5903272448334166185L;

    private transient Object obj;
    private transient String objectString;

    /**
     * Creates the ObjectMessage.
     *
     * @param obj The Object to format.
     */
    public DesignedObjectMessage(final Object obj) {
        this.obj = obj == null ? "null" : obj;
    }

    /**
     * Returns the formatted object message.
     *
     * @return the formatted object message.
     */
    @Override
    public String getFormattedMessage() {
        // LOG4J2-763: cache formatted string in case obj changes later
        if (objectString == null) {
            objectString = String.valueOf(obj);
        }
        return objectString;
    }

    @Override
    public void formatTo(final StringBuilder buffer) {
        if (objectString != null) { //
            buffer.append(objectString);
        } else {
            StringBuilders.appendValue(buffer, obj);
        }
        //自定义特殊处理
        MatchAndReplaceUtil.myPostProcess(buffer);

    }

    /**
     * Returns the object formatted using its toString method.
     *
     * @return the String representation of the object.
     */
    @Override
    public String getFormat() {
        return getFormattedMessage();
    }

    /**
     * Returns the object parameter.
     *
     * @return The object.
     * @since 2.7
     */
    public Object getParameter() {
        return obj;
    }

    /**
     * Returns the object as if it were a parameter.
     *
     * @return The object.
     */
    @Override
    public Object[] getParameters() {
        return new Object[] {obj};
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final DesignedObjectMessage that = (DesignedObjectMessage) o;
        return obj == null ? that.obj == null : equalObjectsOrStrings(obj, that.obj);
    }

    private boolean equalObjectsOrStrings(final Object left, final Object right) {
        return left.equals(right) || String.valueOf(left).equals(String.valueOf(right));
    }

    @Override
    public int hashCode() {
        return obj != null ? obj.hashCode() : 0;
    }

    @Override
    public String toString() {
        return getFormattedMessage();
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        if (obj instanceof Serializable) {
            out.writeObject(obj);
        } else {
            out.writeObject(String.valueOf(obj));
        }
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        obj = in.readObject();
    }

    /**
     * Gets the message if it is a throwable.
     *
     * @return the message if it is a throwable.
     */
    @Override
    public Throwable getThrowable() {
        return obj instanceof Throwable ? (Throwable) obj : null;
    }
}
