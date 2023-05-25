package it.polimi.ingsw.utils;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private final String logFileName;

    public Logger() {
        logFileName = generateLogFileName();
    }

    public void log(String message) {
        writeLogEntry(message);
    }

    public void log(Exception exception) {
        String exceptionStackTrace = getStackTraceAsString(exception);
        writeLogEntry(exceptionStackTrace);
    }

    private String generateLogFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = dateFormat.format(new Date());
        return "log_" + timestamp + ".txt";
    }

    private void writeLogEntry(String logEntry) {
        try (FileWriter fileWriter = new FileWriter(logFileName, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(logEntry);
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }

    private String getStackTraceAsString(Exception exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}