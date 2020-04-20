package com.yusys.log4j2.extender.logger;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.Constants;
import org.apache.logging.log4j.util.StringBuilderFormattable;
import org.apache.logging.log4j.util.StringBuilders;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 重写formatTo方法，增加自定义特殊处理
 */
public class DesignedParameterizedMessage implements Message, StringBuilderFormattable {

    private static final int DEFAULT_STRING_BUILDER_SIZE = 255;

    /**
     * Prefix for recursion.
     */
    public static final String RECURSION_PREFIX = ParameterFormatter.RECURSION_PREFIX;
    /**
     * Suffix for recursion.
     */
    public static final String RECURSION_SUFFIX = ParameterFormatter.RECURSION_SUFFIX;

    /**
     * Prefix for errors.
     */
    public static final String ERROR_PREFIX = ParameterFormatter.ERROR_PREFIX;

    /**
     * Separator for errors.
     */
    public static final String ERROR_SEPARATOR = ParameterFormatter.ERROR_SEPARATOR;

    /**
     * Separator for error messages.
     */
    public static final String ERROR_MSG_SEPARATOR = ParameterFormatter.ERROR_MSG_SEPARATOR;

    /**
     * Suffix for errors.
     */
    public static final String ERROR_SUFFIX = ParameterFormatter.ERROR_SUFFIX;

    private static final long serialVersionUID = -665975803997290697L;

    private static final int HASHVAL = 31;

    // storing JDK classes in ThreadLocals does not cause memory leaks in web apps, so this is okay
    private static ThreadLocal<StringBuilder> threadLocalStringBuilder = new ThreadLocal<>();

    private String messagePattern;
    private transient Object[] argArray;

    private String formattedMessage;
    private transient Throwable throwable;
    private int[] indices;
    private int usedCount;

    /**
     * Creates a parameterized message.
     * @param messagePattern The message "format" string. This will be a String containing "{}" placeholders
     * where parameters should be substituted.
     * @param arguments The arguments for substitution.
     * @param throwable A Throwable.
     * @deprecated Use constructor ParameterizedMessage(String, Object[], Throwable) instead
     */
    @Deprecated
    public DesignedParameterizedMessage(final String messagePattern, final String[] arguments, final Throwable throwable) {
        this.argArray = arguments;
        this.throwable = throwable;
        init(messagePattern);
    }

    /**
     * Creates a parameterized message.
     * @param messagePattern The message "format" string. This will be a String containing "{}" placeholders
     * where parameters should be substituted.
     * @param arguments The arguments for substitution.
     * @param throwable A Throwable.
     */
    public DesignedParameterizedMessage(final String messagePattern, final Object[] arguments, final Throwable throwable) {
        this.argArray = arguments;
        this.throwable = throwable;
        init(messagePattern);
    }

    /**
     * Constructs a ParameterizedMessage which contains the arguments converted to String as well as an optional
     * Throwable.
     *
     * <p>If the last argument is a Throwable and is NOT used up by a placeholder in the message pattern it is returned
     * in {@link #getThrowable()} and won't be contained in the created String[].
     * If it is used up {@link #getThrowable()} will return null even if the last argument was a Throwable!</p>
     *
     * @param messagePattern the message pattern that to be checked for placeholders.
     * @param arguments      the argument array to be converted.
     */
    public DesignedParameterizedMessage(final String messagePattern, final Object... arguments) {
        this.argArray = arguments;
        init(messagePattern);
    }

    /**
     * Constructor with a pattern and a single parameter.
     * @param messagePattern The message pattern.
     * @param arg The parameter.
     */
    public DesignedParameterizedMessage(final String messagePattern, final Object arg) {
        this(messagePattern, new Object[]{arg});
    }

    /**
     * Constructor with a pattern and two parameters.
     * @param messagePattern The message pattern.
     * @param arg0 The first parameter.
     * @param arg1 The second parameter.
     */
    public DesignedParameterizedMessage(final String messagePattern, final Object arg0, final Object arg1) {
        this(messagePattern, new Object[]{arg0, arg1});
    }

    private void init(final String messagePattern) {
        this.messagePattern = messagePattern;
        final int len = Math.max(1, messagePattern == null ? 0 : messagePattern.length() >> 1); // divide by 2
        this.indices = new int[len]; // LOG4J2-1542 ensure non-zero array length
        final int placeholders = ParameterFormatter.countArgumentPlaceholders2(messagePattern, indices);
        initThrowable(argArray, placeholders);
        this.usedCount = Math.min(placeholders, argArray == null ? 0 : argArray.length);
    }

    private void initThrowable(final Object[] params, final int usedParams) {
        if (params != null) {
            final int argCount = params.length;
            if (usedParams < argCount && this.throwable == null && params[argCount - 1] instanceof Throwable) {
                this.throwable = (Throwable) params[argCount - 1];
            }
        }
    }

    /**
     * Returns the message pattern.
     * @return the message pattern.
     */
    @Override
    public String getFormat() {
        return messagePattern;
    }

    /**
     * Returns the message parameters.
     * @return the message parameters.
     */
    @Override
    public Object[] getParameters() {
        return argArray;
    }

    /**
     * Returns the Throwable that was given as the last argument, if any.
     * It will not survive serialization. The Throwable exists as part of the message
     * primarily so that it can be extracted from the end of the list of parameters
     * and then be added to the LogEvent. As such, the Throwable in the event should
     * not be used once the LogEvent has been constructed.
     *
     * @return the Throwable, if any.
     */
    @Override
    public Throwable getThrowable() {
        return throwable;
    }

    /**
     * Returns the formatted message.
     * @return the formatted message.
     */
    @Override
    public String getFormattedMessage() {
        if (formattedMessage == null) {
            final StringBuilder buffer = getThreadLocalStringBuilder();
            formatTo(buffer);
            formattedMessage = buffer.toString();
            StringBuilders.trimToMaxSize(buffer, Constants.MAX_REUSABLE_MESSAGE_SIZE);
        }
        return formattedMessage;
    }

    private static StringBuilder getThreadLocalStringBuilder() {
        StringBuilder buffer = threadLocalStringBuilder.get();
        if (buffer == null) {
            buffer = new StringBuilder(DEFAULT_STRING_BUILDER_SIZE);
            threadLocalStringBuilder.set(buffer);
        }
        buffer.setLength(0);
        return buffer;
    }

    @Override
    public void formatTo(final StringBuilder buffer) {
        if (formattedMessage != null) {
            buffer.append(formattedMessage);
        } else {
            if (indices[0] < 0) {
                ParameterFormatter.formatMessage(buffer, messagePattern, argArray, usedCount);
            } else {
                ParameterFormatter.formatMessage2(buffer, messagePattern, argArray, usedCount, indices);
            }
        }
        //自定义特殊处理
        MatchAndReplaceUtil.myPostProcess(buffer);

    }

    /**
     * Replace placeholders in the given messagePattern with arguments.
     *
     * @param messagePattern the message pattern containing placeholders.
     * @param arguments      the arguments to be used to replace placeholders.
     * @return the formatted message.
     */
    public static String format(final String messagePattern, final Object[] arguments) {
        return ParameterFormatter.format(messagePattern, arguments);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final DesignedParameterizedMessage that = (DesignedParameterizedMessage) o;

        if (messagePattern != null ? !messagePattern.equals(that.messagePattern) : that.messagePattern != null) {
            return false;
        }
        if (!Arrays.equals(this.argArray, that.argArray)) {
            return false;
        }
        //if (throwable != null ? !throwable.equals(that.throwable) : that.throwable != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = messagePattern != null ? messagePattern.hashCode() : 0;
        result = HASHVAL * result + (argArray != null ? Arrays.hashCode(argArray) : 0);
        return result;
    }

    /**
     * Counts the number of unescaped placeholders in the given messagePattern.
     *
     * @param messagePattern the message pattern to be analyzed.
     * @return the number of unescaped placeholders.
     */
    public static int countArgumentPlaceholders(final String messagePattern) {
        return ParameterFormatter.countArgumentPlaceholders(messagePattern);
    }

    /**
     * This method performs a deep toString of the given Object.
     * Primitive arrays are converted using their respective Arrays.toString methods while
     * special handling is implemented for "container types", i.e. Object[], Map and Collection because those could
     * contain themselves.
     * <p>
     * It should be noted that neither AbstractMap.toString() nor AbstractCollection.toString() implement such a
     * behavior. They only check if the container is directly contained in itself, but not if a contained container
     * contains the original one. Because of that, Arrays.toString(Object[]) isn't safe either.
     * Confusing? Just read the last paragraph again and check the respective toString() implementation.
     * </p>
     * <p>
     * This means, in effect, that logging would produce a usable output even if an ordinary System.out.println(o)
     * would produce a relatively hard-to-debug StackOverflowError.
     * </p>
     * @param o The object.
     * @return The String representation.
     */
    public static String deepToString(final Object o) {
        return ParameterFormatter.deepToString(o);
    }

    /**
     * This method returns the same as if Object.toString() would not have been
     * overridden in obj.
     * <p>
     * Note that this isn't 100% secure as collisions can always happen with hash codes.
     * </p>
     * <p>
     * Copied from Object.hashCode():
     * </p>
     * <blockquote>
     * As much as is reasonably practical, the hashCode method defined by
     * class {@code Object} does return distinct integers for distinct
     * objects. (This is typically implemented by converting the internal
     * address of the object into an integer, but this implementation
     * technique is not required by the Java&#8482; programming language.)
     * </blockquote>
     *
     * @param obj the Object that is to be converted into an identity string.
     * @return the identity string as also defined in Object.toString()
     */
    public static String identityToString(final Object obj) {
        return ParameterFormatter.identityToString(obj);
    }

    @Override
    public String toString() {
        return "ParameterizedMessage[messagePattern=" + messagePattern + ", stringArgs=" +
                Arrays.toString(argArray) + ", throwable=" + throwable + ']';
    }
    static final class ParameterFormatter {
        /**
         * Prefix for recursion.
         */
        static final String RECURSION_PREFIX = "[...";
        /**
         * Suffix for recursion.
         */
        static final String RECURSION_SUFFIX = "...]";

        /**
         * Prefix for errors.
         */
        static final String ERROR_PREFIX = "[!!!";
        /**
         * Separator for errors.
         */
        static final String ERROR_SEPARATOR = "=>";
        /**
         * Separator for error messages.
         */
        static final String ERROR_MSG_SEPARATOR = ":";
        /**
         * Suffix for errors.
         */
        static final String ERROR_SUFFIX = "!!!]";

        private static final char DELIM_START = '{';
        private static final char DELIM_STOP = '}';
        private static final char ESCAPE_CHAR = '\\';

        private static ThreadLocal<SimpleDateFormat> threadLocalSimpleDateFormat = new ThreadLocal<>();

        private ParameterFormatter() {
        }

        /**
         * Counts the number of unescaped placeholders in the given messagePattern.
         *
         * @param messagePattern the message pattern to be analyzed.
         * @return the number of unescaped placeholders.
         */
        static int countArgumentPlaceholders(final String messagePattern) {
            if (messagePattern == null) {
                return 0;
            }
            final int length = messagePattern.length();
            int result = 0;
            boolean isEscaped = false;
            for (int i = 0; i < length - 1; i++) {
                final char curChar = messagePattern.charAt(i);
                if (curChar == ESCAPE_CHAR) {
                    isEscaped = !isEscaped;
                } else if (curChar == DELIM_START) {
                    if (!isEscaped && messagePattern.charAt(i + 1) == DELIM_STOP) {
                        result++;
                        i++;
                    }
                    isEscaped = false;
                } else {
                    isEscaped = false;
                }
            }
            return result;
        }

        /**
         * Counts the number of unescaped placeholders in the given messagePattern.
         *
         * @param messagePattern the message pattern to be analyzed.
         * @return the number of unescaped placeholders.
         */
        static int countArgumentPlaceholders2(final String messagePattern, final int[] indices) {
            if (messagePattern == null) {
                return 0;
            }
            final int length = messagePattern.length();
            int result = 0;
            boolean isEscaped = false;
            for (int i = 0; i < length - 1; i++) {
                final char curChar = messagePattern.charAt(i);
                if (curChar == ESCAPE_CHAR) {
                    isEscaped = !isEscaped;
                    indices[0] = -1; // escaping means fast path is not available...
                    result++;
                } else if (curChar == DELIM_START) {
                    if (!isEscaped && messagePattern.charAt(i + 1) == DELIM_STOP) {
                        indices[result] = i;
                        result++;
                        i++;
                    }
                    isEscaped = false;
                } else {
                    isEscaped = false;
                }
            }
            return result;
        }

        /**
         * Counts the number of unescaped placeholders in the given messagePattern.
         *
         * @param messagePattern the message pattern to be analyzed.
         * @return the number of unescaped placeholders.
         */
        static int countArgumentPlaceholders3(final char[] messagePattern, final int length, final int[] indices) {
            int result = 0;
            boolean isEscaped = false;
            for (int i = 0; i < length - 1; i++) {
                final char curChar = messagePattern[i];
                if (curChar == ESCAPE_CHAR) {
                    isEscaped = !isEscaped;
                } else if (curChar == DELIM_START) {
                    if (!isEscaped && messagePattern[i + 1] == DELIM_STOP) {
                        indices[result] = i;
                        result++;
                        i++;
                    }
                    isEscaped = false;
                } else {
                    isEscaped = false;
                }
            }
            return result;
        }

        /**
         * Replace placeholders in the given messagePattern with arguments.
         *
         * @param messagePattern the message pattern containing placeholders.
         * @param arguments      the arguments to be used to replace placeholders.
         * @return the formatted message.
         */
        static String format(final String messagePattern, final Object[] arguments) {
            final StringBuilder result = new StringBuilder();
            final int argCount = arguments == null ? 0 : arguments.length;
            formatMessage(result, messagePattern, arguments, argCount);
            return result.toString();
        }

        /**
         * Replace placeholders in the given messagePattern with arguments.
         *
         * @param buffer the buffer to write the formatted message into
         * @param messagePattern the message pattern containing placeholders.
         * @param arguments      the arguments to be used to replace placeholders.
         */
        static void formatMessage2(final StringBuilder buffer, final String messagePattern,
                                   final Object[] arguments, final int argCount, final int[] indices) {
            if (messagePattern == null || arguments == null || argCount == 0) {
                buffer.append(messagePattern);
                return;
            }
            int previous = 0;
            for (int i = 0; i < argCount; i++) {
                buffer.append(messagePattern, previous, indices[i]);
                previous = indices[i] + 2;
                recursiveDeepToString(arguments[i], buffer, null);

            }
            buffer.append(messagePattern, previous, messagePattern.length());
        }

        /**
         * Replace placeholders in the given messagePattern with arguments.
         *
         * @param buffer the buffer to write the formatted message into
         * @param messagePattern the message pattern containing placeholders.
         * @param arguments      the arguments to be used to replace placeholders.
         */
        static void formatMessage3(final StringBuilder buffer, final char[] messagePattern, final int patternLength,
                                   final Object[] arguments, final int argCount, final int[] indices) {
            if (messagePattern == null) {
                return;
            }
            if (arguments == null || argCount == 0) {
                buffer.append(messagePattern);
                return;
            }
            int previous = 0;
            for (int i = 0; i < argCount; i++) {
                buffer.append(messagePattern, previous, indices[i]);
                previous = indices[i] + 2;
                recursiveDeepToString(arguments[i], buffer, null);
            }
            buffer.append(messagePattern, previous, patternLength);
        }

        /**
         * Replace placeholders in the given messagePattern with arguments.
         *
         * @param buffer the buffer to write the formatted message into
         * @param messagePattern the message pattern containing placeholders.
         * @param arguments      the arguments to be used to replace placeholders.
         */
        static void formatMessage(final StringBuilder buffer, final String messagePattern,
                                  final Object[] arguments, final int argCount) {
            if (messagePattern == null || arguments == null || argCount == 0) {
                buffer.append(messagePattern);
                return;
            }
            int escapeCounter = 0;
            int currentArgument = 0;
            int i = 0;
            final int len = messagePattern.length();
            for (; i < len - 1; i++) { // last char is excluded from the loop
                final char curChar = messagePattern.charAt(i);
                if (curChar == ESCAPE_CHAR) {
                    escapeCounter++;
                } else {
                    if (isDelimPair(curChar, messagePattern, i)) { // looks ahead one char
                        i++;

                        // write escaped escape chars
                        writeEscapedEscapeChars(escapeCounter, buffer);

                        if (isOdd(escapeCounter)) {
                            // i.e. escaped: write escaped escape chars
                            writeDelimPair(buffer);
                        } else {
                            // unescaped
                            writeArgOrDelimPair(arguments, argCount, currentArgument, buffer);
                            currentArgument++;
                        }
                    } else {
                        handleLiteralChar(buffer, escapeCounter, curChar);
                    }
                    escapeCounter = 0;
                }
            }
            handleRemainingCharIfAny(messagePattern, len, buffer, escapeCounter, i);
        }

        /**
         * Returns {@code true} if the specified char and the char at {@code curCharIndex + 1} in the specified message
         * pattern together form a "{}" delimiter pair, returns {@code false} otherwise.
         */
        // Profiling showed this method is important to log4j performance. Modify with care!
        // 22 bytes (allows immediate JVM inlining: < 35 bytes) LOG4J2-1096
        private static boolean isDelimPair(final char curChar, final String messagePattern, final int curCharIndex) {
            return curChar == DELIM_START && messagePattern.charAt(curCharIndex + 1) == DELIM_STOP;
        }

        /**
         * Detects whether the message pattern has been fully processed or if an unprocessed character remains and processes
         * it if necessary, returning the resulting position in the result char array.
         */
        // Profiling showed this method is important to log4j performance. Modify with care!
        // 28 bytes (allows immediate JVM inlining: < 35 bytes) LOG4J2-1096
        private static void handleRemainingCharIfAny(final String messagePattern, final int len,
                                                     final StringBuilder buffer, final int escapeCounter, final int i) {
            if (i == len - 1) {
                final char curChar = messagePattern.charAt(i);
                handleLastChar(buffer, escapeCounter, curChar);
            }
        }

        /**
         * Processes the last unprocessed character and returns the resulting position in the result char array.
         */
        // Profiling showed this method is important to log4j performance. Modify with care!
        // 28 bytes (allows immediate JVM inlining: < 35 bytes) LOG4J2-1096
        private static void handleLastChar(final StringBuilder buffer, final int escapeCounter, final char curChar) {
            if (curChar == ESCAPE_CHAR) {
                writeUnescapedEscapeChars(escapeCounter + 1, buffer);
            } else {
                handleLiteralChar(buffer, escapeCounter, curChar);
            }
        }

        /**
         * Processes a literal char (neither an '\' escape char nor a "{}" delimiter pair) and returns the resulting
         * position.
         */
        // Profiling showed this method is important to log4j performance. Modify with care!
        // 16 bytes (allows immediate JVM inlining: < 35 bytes) LOG4J2-1096
        private static void handleLiteralChar(final StringBuilder buffer, final int escapeCounter, final char curChar) {
            // any other char beside ESCAPE or DELIM_START/STOP-combo
            // write unescaped escape chars
            writeUnescapedEscapeChars(escapeCounter, buffer);
            buffer.append(curChar);
        }

        /**
         * Writes "{}" to the specified result array at the specified position and returns the resulting position.
         */
        // Profiling showed this method is important to log4j performance. Modify with care!
        // 18 bytes (allows immediate JVM inlining: < 35 bytes) LOG4J2-1096
        private static void writeDelimPair(final StringBuilder buffer) {
            buffer.append(DELIM_START);
            buffer.append(DELIM_STOP);
        }

        /**
         * Returns {@code true} if the specified parameter is odd.
         */
        // Profiling showed this method is important to log4j performance. Modify with care!
        // 11 bytes (allows immediate JVM inlining: < 35 bytes) LOG4J2-1096
        private static boolean isOdd(final int number) {
            return (number & 1) == 1;
        }

        /**
         * Writes a '\' char to the specified result array (starting at the specified position) for each <em>pair</em> of
         * '\' escape chars encountered in the message format and returns the resulting position.
         */
        // Profiling showed this method is important to log4j performance. Modify with care!
        // 11 bytes (allows immediate JVM inlining: < 35 bytes) LOG4J2-1096
        private static void writeEscapedEscapeChars(final int escapeCounter, final StringBuilder buffer) {
            final int escapedEscapes = escapeCounter >> 1; // divide by two
            writeUnescapedEscapeChars(escapedEscapes, buffer);
        }

        /**
         * Writes the specified number of '\' chars to the specified result array (starting at the specified position) and
         * returns the resulting position.
         */
        // Profiling showed this method is important to log4j performance. Modify with care!
        // 20 bytes (allows immediate JVM inlining: < 35 bytes) LOG4J2-1096
        private static void writeUnescapedEscapeChars(int escapeCounter, final StringBuilder buffer) {
            while (escapeCounter > 0) {
                buffer.append(ESCAPE_CHAR);
                escapeCounter--;
            }
        }

        /**
         * Appends the argument at the specified argument index (or, if no such argument exists, the "{}" delimiter pair) to
         * the specified result char array at the specified position and returns the resulting position.
         */
        // Profiling showed this method is important to log4j performance. Modify with care!
        // 25 bytes (allows immediate JVM inlining: < 35 bytes) LOG4J2-1096
        private static void writeArgOrDelimPair(final Object[] arguments, final int argCount, final int currentArgument,
                                                final StringBuilder buffer) {
            if (currentArgument < argCount) {
                recursiveDeepToString(arguments[currentArgument], buffer, null);
            } else {
                writeDelimPair(buffer);
            }
        }

        /**
         * This method performs a deep toString of the given Object.
         * Primitive arrays are converted using their respective Arrays.toString methods while
         * special handling is implemented for "container types", i.e. Object[], Map and Collection because those could
         * contain themselves.
         * <p>
         * It should be noted that neither AbstractMap.toString() nor AbstractCollection.toString() implement such a
         * behavior. They only check if the container is directly contained in itself, but not if a contained container
         * contains the original one. Because of that, Arrays.toString(Object[]) isn't safe either.
         * Confusing? Just read the last paragraph again and check the respective toString() implementation.
         * </p>
         * <p>
         * This means, in effect, that logging would produce a usable output even if an ordinary System.out.println(o)
         * would produce a relatively hard-to-debug StackOverflowError.
         * </p>
         * @param o The object.
         * @return The String representation.
         */
        static String deepToString(final Object o) {
            if (o == null) {
                return null;
            }
            // Check special types to avoid unnecessary StringBuilder usage
            if (o instanceof String) {
                return (String) o;
            }
            if (o instanceof Integer) {
                return Integer.toString((Integer) o);
            }
            if (o instanceof Long) {
                return Long.toString((Long) o);
            }
            if (o instanceof Double) {
                return Double.toString((Double) o);
            }
            if (o instanceof Boolean) {
                return Boolean.toString((Boolean) o);
            }
            if (o instanceof Character) {
                return Character.toString((Character) o);
            }
            if (o instanceof Short) {
                return Short.toString((Short) o);
            }
            if (o instanceof Float) {
                return Float.toString((Float) o);
            }
            if (o instanceof Byte) {
                return Byte.toString((Byte) o);
            }
            final StringBuilder str = new StringBuilder();
            recursiveDeepToString(o, str, null);
            return str.toString();
        }

        /**
         * This method performs a deep toString of the given Object.
         * Primitive arrays are converted using their respective Arrays.toString methods while
         * special handling is implemented for "container types", i.e. Object[], Map and Collection because those could
         * contain themselves.
         * <p>
         * dejaVu is used in case of those container types to prevent an endless recursion.
         * </p>
         * <p>
         * It should be noted that neither AbstractMap.toString() nor AbstractCollection.toString() implement such a
         * behavior.
         * They only check if the container is directly contained in itself, but not if a contained container contains the
         * original one. Because of that, Arrays.toString(Object[]) isn't safe either.
         * Confusing? Just read the last paragraph again and check the respective toString() implementation.
         * </p>
         * <p>
         * This means, in effect, that logging would produce a usable output even if an ordinary System.out.println(o)
         * would produce a relatively hard-to-debug StackOverflowError.
         * </p>
         *
         * @param o      the Object to convert into a String
         * @param str    the StringBuilder that o will be appended to
         * @param dejaVu a list of container identities that were already used.
         */
        static void recursiveDeepToString(final Object o, final StringBuilder str, final Set<String> dejaVu) {
            if (appendSpecialTypes(o, str)) {
                return;
            }
            if (isMaybeRecursive(o)) {
                appendPotentiallyRecursiveValue(o, str, dejaVu);
            } else {
                tryObjectToString(o, str);
            }
        }

        private static boolean appendSpecialTypes(final Object o, final StringBuilder str) {
            return StringBuilders.appendSpecificTypes(str, o) || appendDate(o, str);
        }

        private static boolean appendDate(final Object o, final StringBuilder str) {
            if (!(o instanceof Date)) {
                return false;
            }
            final Date date = (Date) o;
            final SimpleDateFormat format = getSimpleDateFormat();
            str.append(format.format(date));
            return true;
        }

        private static SimpleDateFormat getSimpleDateFormat() {
            SimpleDateFormat result = threadLocalSimpleDateFormat.get();
            if (result == null) {
                result = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                threadLocalSimpleDateFormat.set(result);
            }
            return result;
        }

        /**
         * Returns {@code true} if the specified object is an array, a Map or a Collection.
         */
        private static boolean isMaybeRecursive(final Object o) {
            return o.getClass().isArray() || o instanceof Map || o instanceof Collection;
        }

        private static void appendPotentiallyRecursiveValue(final Object o, final StringBuilder str,
                                                            final Set<String> dejaVu) {
            final Class<?> oClass = o.getClass();
            if (oClass.isArray()) {
                appendArray(o, str, dejaVu, oClass);
            } else if (o instanceof Map) {
                appendMap(o, str, dejaVu);
            } else if (o instanceof Collection) {
                appendCollection(o, str, dejaVu);
            }
        }

        private static void appendArray(final Object o, final StringBuilder str, Set<String> dejaVu,
                                        final Class<?> oClass) {
            if (oClass == byte[].class) {
                str.append(Arrays.toString((byte[]) o));
            } else if (oClass == short[].class) {
                str.append(Arrays.toString((short[]) o));
            } else if (oClass == int[].class) {
                str.append(Arrays.toString((int[]) o));
            } else if (oClass == long[].class) {
                str.append(Arrays.toString((long[]) o));
            } else if (oClass == float[].class) {
                str.append(Arrays.toString((float[]) o));
            } else if (oClass == double[].class) {
                str.append(Arrays.toString((double[]) o));
            } else if (oClass == boolean[].class) {
                str.append(Arrays.toString((boolean[]) o));
            } else if (oClass == char[].class) {
                str.append(Arrays.toString((char[]) o));
            } else {
                if (dejaVu == null) {
                    dejaVu = new HashSet<>();
                }
                // special handling of container Object[]
                final String id = identityToString(o);
                if (dejaVu.contains(id)) {
                    str.append(RECURSION_PREFIX).append(id).append(RECURSION_SUFFIX);
                } else {
                    dejaVu.add(id);
                    final Object[] oArray = (Object[]) o;
                    str.append('[');
                    boolean first = true;
                    for (final Object current : oArray) {
                        if (first) {
                            first = false;
                        } else {
                            str.append(", ");
                        }
                        recursiveDeepToString(current, str, new HashSet<>(dejaVu));
                    }
                    str.append(']');
                }
                //str.append(Arrays.deepToString((Object[]) o));
            }
        }

        private static void appendMap(final Object o, final StringBuilder str, Set<String> dejaVu) {
            // special handling of container Map
            if (dejaVu == null) {
                dejaVu = new HashSet<>();
            }
            final String id = identityToString(o);
            if (dejaVu.contains(id)) {
                str.append(RECURSION_PREFIX).append(id).append(RECURSION_SUFFIX);
            } else {
                dejaVu.add(id);
                final Map<?, ?> oMap = (Map<?, ?>) o;
                str.append('{');
                boolean isFirst = true;
                for (final Object o1 : oMap.entrySet()) {
                    final Map.Entry<?, ?> current = (Map.Entry<?, ?>) o1;
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        str.append(", ");
                    }
                    final Object key = current.getKey();
                    final Object value = current.getValue();
                    recursiveDeepToString(key, str, new HashSet<>(dejaVu));
                    str.append('=');
                    recursiveDeepToString(value, str, new HashSet<>(dejaVu));
                }
                str.append('}');
            }
        }

        private static void appendCollection(final Object o, final StringBuilder str, Set<String> dejaVu) {
            // special handling of container Collection
            if (dejaVu == null) {
                dejaVu = new HashSet<>();
            }
            final String id = identityToString(o);
            if (dejaVu.contains(id)) {
                str.append(RECURSION_PREFIX).append(id).append(RECURSION_SUFFIX);
            } else {
                dejaVu.add(id);
                final Collection<?> oCol = (Collection<?>) o;
                str.append('[');
                boolean isFirst = true;
                for (final Object anOCol : oCol) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        str.append(", ");
                    }
                    recursiveDeepToString(anOCol, str, new HashSet<>(dejaVu));
                }
                str.append(']');
            }
        }

        private static void tryObjectToString(final Object o, final StringBuilder str) {
            // it's just some other Object, we can only use toString().
            try {
                str.append(o.toString());
            } catch (final Throwable t) {
                handleErrorInObjectToString(o, str, t);
            }
        }

        private static void handleErrorInObjectToString(final Object o, final StringBuilder str, final Throwable t) {
            str.append(ERROR_PREFIX);
            str.append(identityToString(o));
            str.append(ERROR_SEPARATOR);
            final String msg = t.getMessage();
            final String className = t.getClass().getName();
            str.append(className);
            if (!className.equals(msg)) {
                str.append(ERROR_MSG_SEPARATOR);
                str.append(msg);
            }
            str.append(ERROR_SUFFIX);
        }

        /**
         * This method returns the same as if Object.toString() would not have been
         * overridden in obj.
         * <p>
         * Note that this isn't 100% secure as collisions can always happen with hash codes.
         * </p>
         * <p>
         * Copied from Object.hashCode():
         * </p>
         * <blockquote>
         * As much as is reasonably practical, the hashCode method defined by
         * class {@code Object} does return distinct integers for distinct
         * objects. (This is typically implemented by converting the internal
         * address of the object into an integer, but this implementation
         * technique is not required by the Java&#8482; programming language.)
         * </blockquote>
         *
         * @param obj the Object that is to be converted into an identity string.
         * @return the identity string as also defined in Object.toString()
         */
        static String identityToString(final Object obj) {
            if (obj == null) {
                return null;
            }
            return obj.getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(obj));
        }

    }
}
