package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Class to output login activity to text file
 */
public class logWriter {

    /**
     * Appends string to text file stating login attempt succeeded including date, time, and location
     * @param dateTime Date/Time
     * @throws IOException IOException
     */
    public static void writeSuccess(String dateTime) throws IOException {

        String filename = "src/main/login_activity.txt";
        FileWriter fileWriter = new FileWriter(filename, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        PrintWriter outputFile = new PrintWriter(bufferedWriter);
        outputFile.println("Successful login attempt at " + dateTime);
        outputFile.close();

    }

    /**
     * Appends string to text file stating login attempt failed including date, time, and location
     * @param dateTime Date/Time
     * @throws IOException IOException
     */
    public static void writeFail(String dateTime) throws IOException {

        String filename = "src/main/login_activity.txt";
        FileWriter fileWriter = new FileWriter(filename, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        PrintWriter outputFile = new PrintWriter(bufferedWriter);
        outputFile.println("Failed login attempt at " + dateTime);
        outputFile.close();

    }
}