/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pconvert;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import pconvert.oo_to_pdf_conversion.ConfigureOOXDesktop;

/**
 *
 * @author riteshpandhurkar
 */
public class Utility {

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        } else {
            return "";
        }
    }

    public static boolean isDirecotryPathExists(File file) {
        return file.exists() && file.isDirectory();
    }

    public static boolean isFilePathExists(File file) {
        return file.exists() && file.isFile();
    }

    // This is done , to forcefully close app, after conversion done.
    public static void doScheduleToCloseApp() {

        ScheduledExecutorService scheduler
                = Executors.newScheduledThreadPool(1);

        final Thread taskToCheckAllFutureCompleted = new Thread(new Runnable() {
            @Override
            public void run() {
                scheduler.shutdown();
                ConfigureOOXDesktop.stopXDesktop();
            }
        });

        int initialDelay = 50;
        int periodicDelay = 10;

        scheduler.scheduleAtFixedRate(taskToCheckAllFutureCompleted, initialDelay, periodicDelay,
                TimeUnit.SECONDS);
    }

}
