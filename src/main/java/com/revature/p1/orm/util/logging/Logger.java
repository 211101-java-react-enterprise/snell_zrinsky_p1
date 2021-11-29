package com.revature.p1.orm.util.logging;

import com.revature.p1.orm.util.logging.types.LogLevel;
import com.revature.p1.orm.util.logging.types.LogPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class Logger {

    private static Logger logger;
    private static final String LOG_FILE = "src/main/resources/log/app.log";

    private final Logger.Printer logPrinter;

    public Logger(Logger.Printer logPrinter) {
        if  (logPrinter == null) {
            this.logPrinter = Logger.Printer.FILE;
        } else {
            this.logPrinter = logPrinter;
        }
    }

    public static Logger getLogger(Logger.Printer logPrinter) {
        return new Logger(logPrinter);
    }

    public void log(Logger.Level logLevel, String msg) {
        String formattedMsg = String.format("[%s]: %s", logLevel.name(), msg);
        if (this.logPrinter.equals(Logger.Printer.CONSOLE)) {
            System.out.println(formattedMsg);
        } else {
            try (Writer logWriter = new FileWriter(Logger.LOG_FILE, true)) {
                logWriter.write(formattedMsg + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public enum Level {
        INFO,
        DEBUG,
        ERROR
    }

    public enum Printer {
        CONSOLE,
        FILE
    }
}
