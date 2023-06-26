package it.polimi.ingsw.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * logger that logs messages in a text file
 */
public class Logger {
    private final String logFolderPath;
    private final String logFileName;

    /**
     * Default constructor that logs in a 'logs' folder
     */
    public Logger() {
        this.logFolderPath = "logs";
        createLogFolderIfNotPresent();
        this.logFileName = generateLogFileName();
    }

    /**
     * Constructor that accepts the folder to log to as a parameter
     * @param logFolderPath the folder to log to
     */
    public Logger(String logFolderPath) {
        this.logFolderPath = logFolderPath;
        createLogFolderIfNotPresent();
        this.logFileName = generateLogFileName();
    }

    /**
     * Logs a message
     * @param message the message to log
     */
    public synchronized void log(String message) {
        writeLogEntry(message);
    }

    /**
     * Logs the stack trace of an exception
     * @param exception the exception to be logged
     */
    public synchronized void log(Exception exception) {
        String exceptionStackTrace = getStackTraceAsString(exception);
        writeLogEntry(exceptionStackTrace);
    }

    /**
     * Generates the file name of the log as 'log_timestamp'
     */
    private synchronized String generateLogFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = dateFormat.format(new Date());
        return "log_" + timestamp + ".txt";
    }

    /**
     * Writes to the log file
     * @param logEntry the message to write
     */
    private synchronized void writeLogEntry(String logEntry) {
        String logFilePath = logFolderPath + File.separator + logFileName;
        try {
            FileWriter fileWriter = new FileWriter(logFilePath, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println((new SimpleDateFormat("yyyyMMdd_HHmmss")).format(new Date()) + ":");
            printWriter.println(logEntry);
            printWriter.close();
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }

    /**
     * Extracts the stacktrace of an exception as string
     * @param exception the exception
     * @return the stacktrace
     */
    private synchronized String getStackTraceAsString(Exception exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    /**
     * Creates the log folder if it doesn't exist
     */
    private synchronized void createLogFolderIfNotPresent() {
        File logFolder = new File(logFolderPath);
        if (!logFolder.exists()) {
            boolean created = logFolder.mkdirs();
            if (!created) {
                System.err.println("Failed to create log folder: " + logFolderPath);
            }
        }
    }
}