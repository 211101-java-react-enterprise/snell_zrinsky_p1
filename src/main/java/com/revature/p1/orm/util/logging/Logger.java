package com.revature.p1.orm.util.logging;

import java.util.HashMap;

public class Logger {

    private static final HashMap<Class<?>,Logger> loggers = new HashMap<>();

    private final Class<?> owner;
    private final LogLevel logLevel;
    private final LogPrinter logPrinter;

    public Logger(Class<?> owner, LogLevel logLevel, LogPrinter logPrinter) {
        this.owner = owner;
        this.logLevel = logLevel;
        this.logPrinter = logPrinter;
    }

    public static Logger getLogger(Class<?> owner) {
        if(!loggers.containsKey(owner)) {
            loggers.put(owner, new Logger(owner, LogLevel.INFO, LogPrinter.CONSOLE));
        }
        return loggers.get(owner);
    }

    public static Logger getLogger(Class<?> owner, LogLevel logLevel) {
        if(!loggers.containsKey(owner)) {
            loggers.put(owner, new Logger(owner, logLevel, LogPrinter.CONSOLE));
        }
        return new Logger(owner, logLevel, LogPrinter.CONSOLE);
    }

    public static Logger getLogger(Class<?> owner, LogLevel logLevel, LogPrinter logPrinter) {
        if(!loggers.containsKey(owner)) {
            loggers.put(owner, new Logger(owner, logLevel, logPrinter));
        }
        return new Logger(owner, logLevel, logPrinter);
    }

    public void log(LogLevel logLevel, String msg) {
        String logMsg = String.format("[%s] [%s] %s", owner.getSimpleName(), logLevel.toString(), msg);
        System.out.println(logMsg);
    }

}
