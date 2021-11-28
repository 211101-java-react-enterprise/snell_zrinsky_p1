package com.revature.p1.orm.logging;

import com.revature.p1.orm.logging.types.LogLevel;
import com.revature.p1.orm.logging.types.LogPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class Logger {

    private static Logger logger;
    private static final String LOG_FILE = "src/main/resources/log/app.log";

    private final LogPrinter logPrinter;

    private Logger(LogPrinter logPrinter) {
        if  (logPrinter == null) {
            this.logPrinter = LogPrinter.FILE;
        } else {
            this.logPrinter = logPrinter;
        }
    }

    public static Logger getLogger(LogPrinter logPrinter) {
        return new Logger(logPrinter);
    }

    public void log(LogLevel logLevel, String msg) {
        String formattedMsg = String.format("[%s]: %s", logLevel.name(), msg);
        if (this.logPrinter.equals(LogPrinter.CONSOLE)) {
            System.out.println(formattedMsg);
        } else {
            try (Writer logWriter = new FileWriter(Logger.LOG_FILE, true)) {
                logWriter.write(formattedMsg + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
