package de.julianhofmann.autospiel.logging;

import java.time.LocalDateTime;

public class Log {
    public static LogLevel logLevel = LogLevel.TRACE;

    public static void trace(String text) {
        if (logLevel.compareTo(LogLevel.TRACE) <= 0) {
            System.out.println("[TRACE]   [" + LocalDateTime.now() + "]: " + text);
        }
    }

    public static void info(String text) {
        if (logLevel.compareTo(LogLevel.INFO) <= 0) {
            System.out.println("[INFO]    [" + LocalDateTime.now() + "]: " + text);
        }
    }

    public static void warn(String text) {
        if (logLevel.compareTo(LogLevel.WARNING) <= 0) {
            System.out.println("[WARNING] [" + LocalDateTime.now() + "]: " + text);
        }
    }

    public static void error(String text) {
        if (logLevel.compareTo(LogLevel.ERROR) <= 0) {
            System.err.println("[ERROR]   [" + LocalDateTime.now() + "]: " + text);
        }
    }

    public static void fatal(String text) {
        if (logLevel.compareTo(LogLevel.FATAL) <= 0) {
            System.err.println("[FATAL]   [" + LocalDateTime.now() + "]: " + text);
        }
    }
}
