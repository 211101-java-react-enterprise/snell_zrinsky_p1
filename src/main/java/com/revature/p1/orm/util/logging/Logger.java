package com.revature.p1.orm.util.logging;

import java.util.HashMap;

public class Logger {

    private static final HashMap<Class<?>,Logger> loggers = new HashMap<>();

    private final Class<?> owner;
    private final Level level;
    private final Printer printer;

    public Logger(Class<?> owner, Level level, Printer printer) {
        this.owner = owner;
        this.level = level;
        this.printer = printer;
    }

    public static Logger getLogger(Class<?> owner) {
        if(!loggers.containsKey(owner)) {
            loggers.put(owner, new Logger(owner, Level.INFO, Printer.CONSOLE));
        }
        return loggers.get(owner);
    }

    public static Logger getLogger(Class<?> owner, Level level) {
        if(!loggers.containsKey(owner)) {
            loggers.put(owner, new Logger(owner, level, Printer.CONSOLE));
        }
        return new Logger(owner, level, Printer.CONSOLE);
    }

    public static Logger getLogger(Class<?> owner, Level level, Printer printer) {
        if(!loggers.containsKey(owner)) {
            loggers.put(owner, new Logger(owner, level, printer));
        }
        return new Logger(owner, level, printer);
    }

    public void log(Level logLevel, String msg) {
        String logMsg = String.format("[%s] [%s] %s", owner.getSimpleName(), logLevel.toString(), msg);
        System.out.println(logMsg);
    }

}
