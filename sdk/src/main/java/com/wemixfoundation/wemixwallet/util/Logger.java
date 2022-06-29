package com.wemixfoundation.wemixwallet.util;

import android.util.Log;

/**
 * Default logger
 * <p/>
 *
 * @author ybjeon
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Logger{
    public enum Level {
        VERBOSE,
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    private Logger() {
    }

    private static Level loggerLevel = Level.INFO;
    private static String defaultTag = "com.wemixwallet.sdk";

    /**
     * Set print log level
     * @param level log level
     */
    public static void setLoggerLevel(Level level) {
        loggerLevel = level;
    }

    /**
     * Set default log tag
     * @param tag tag name
     */
    public static void setDefaultTag(String tag) {
        defaultTag = tag;
    }

    private static boolean isPrintLoggerLevel(Level level) {
        return level.compareTo(loggerLevel) >= 0;
    }

    public static boolean isVerboseEnabled() {
        return isPrintLoggerLevel(Level.VERBOSE);
    }

    public static boolean isDebugEnabled() {
        return isPrintLoggerLevel(Level.DEBUG);
    }

    public static boolean isInfoEnabled() {
        return isPrintLoggerLevel(Level.INFO);
    }

    public static boolean isWarnEnabled() {
        return isPrintLoggerLevel(Level.WARN);
    }

    public static boolean isErrorEnabled() {
        return isPrintLoggerLevel(Level.ERROR);
    }

    public static void verbose(String log) {
        if (isVerboseEnabled()) {
            Log.v(defaultTag, log);
        }
    }

    public static void verbose(String log, Throwable t) {
        if (isVerboseEnabled()) {
            Log.v(defaultTag, log, t);
        }
    }

    public static void debug(String log) {
        if (isDebugEnabled()) {
            Log.d(defaultTag, log);
        }
    }

    public static void debug(String log, Throwable t) {
        if (isDebugEnabled()) {
            Log.d(defaultTag, log, t);
        }
    }

    public static void info(String log) {
        if (isInfoEnabled()) {
            Log.i(defaultTag, log);
        }
    }

    public static void info(String log, Throwable t) {
        if (isInfoEnabled()) {
            Log.i(defaultTag, log, t);
        }
    }

    public static void warn(String log) {
        if (isWarnEnabled()) {
            Log.w(defaultTag, log);
        }
    }

    public static void warn(String log, Throwable t) {
        if (isWarnEnabled()) {
            Log.w(defaultTag, log, t);
        }
    }

    public static void error(String log) {
        if (isErrorEnabled()) {
            Log.e(defaultTag, log);
        }
    }

    public static void error(String log, Throwable t) {
        if (isErrorEnabled()) {
            Log.e(defaultTag, log, t);
        }
    }
}
