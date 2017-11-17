/*
 *
 * Copyright (c), Trimble Construction Logistics
 *
 *
 */
package hackathon.trimble.trackme.utils;


//region import directives

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
//endregion import directives

public class InternalFileStorageHelper {

    //region public methods

    /**
     * Determines whether or not the specified file exists
     * within the device's internal storage
     *
     * @param context  Android context
     * @param fileName File name (no path)
     * @return True if the file exists, False otherwise
     * @throws IllegalArgumentException Thrown if fileName is null or empty
     */
    @CheckResult
    public static boolean exists(@NonNull final Context context,
                                 @NonNull final String fileName) {
        boolean exist=false;
        // validate arguments
        if(!TextUtils.isEmpty(fileName) ){

            // create file object around file path/name
            File file = new File(context.getFilesDir(), fileName);
            exist=file.exists();
        }
        // return to caller
        return exist;
    }

    /**
     * Reads the specified file into a String
     *
     * @param context  Android context
     * @param fileName File name (no path)
     * @throws IllegalArgumentException Thrown if fileName is null or empty
     * @throws IOException                Thrown if exception occurs while reading the file
     */
    @CheckResult
    @SuppressWarnings("WeakerAccess")
    public static String readStringFromFile(@NonNull final Context context,
                                            @NonNull final String fileName)
            throws IllegalArgumentException, IOException {

        // read the file and return its contents in a String
        return new Scanner(new File(context.getFilesDir(), fileName), "UTF-8").useDelimiter("\\Z").next();
    }

    /**
     * Writes the specified text to the specified file
     *
     * @param context     Android context
     * @param fileName    File name (no path)
     * @param textToWrite Text to write
     * @throws IllegalArgumentException Thrown if fileName or textToWrite are null or empty
     * @throws IOException                Thrown if exception occurs while writing the text to the file
     */
    @SuppressWarnings("WeakerAccess")
    public static void writeStringToFile(@NonNull final Context context,
                                         @NonNull final String fileName,
                                         @NonNull final String textToWrite)
            throws IllegalArgumentException, IOException {

        // write out the text
        try (PrintWriter out = new PrintWriter(new File(context.getFilesDir(), fileName), "UTF-8")) {
            out.println(textToWrite);
        } catch (Exception ex) {

        }
    }

    /**
     * Appends the specified text to the end of the specified file
     *
     * @param context     Android context
     * @param fileName    File name (no path)
     * @param textToWrite Text to write
     * @throws IllegalArgumentException Thrown if fileName or textToWrite are null or empty
     * @throws IOException                Thrown if exception occurs while writing the text to the file
     */
    @SuppressWarnings("WeakerAccess")
    public static void appendStringToFile(@NonNull final Context context,
                                          @NonNull final String fileName,
                                          @NonNull final String textToWrite)
            throws IllegalArgumentException, IOException {

        // append the text to the file
        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(new File(context.getFilesDir(), fileName), true), StandardCharsets.UTF_8))) {
            bufferedWriter.write(textToWrite);
            bufferedWriter.newLine();
        } catch (Exception ex) {

        }
    }

    /**
     * Deletes the specified file
     *
     * @param context  Android context
     * @param fileName File name (no path)
     * @return True upon success
     */
    @CheckResult
    @SuppressWarnings("WeakerAccess")
    public static boolean deleteFile(@NonNull final Context context,
                                     @NonNull final String fileName) {
        //return value
        boolean isDeleted = false;

        // Delete the file from internal storage.
        File fileToDelete = new File(context.getFilesDir(), fileName);
        if (fileToDelete.exists()) {
            isDeleted = fileToDelete.delete();
        }
        return isDeleted;
    }
    //endregion public methods
}
