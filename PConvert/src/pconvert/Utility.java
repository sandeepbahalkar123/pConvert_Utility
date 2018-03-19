/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pconvert;

import java.io.File;

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

}
