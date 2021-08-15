package com.automation.lucidhearing.android.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;

public class FileUtils {

     public static void setFilePermissionToExecutable(String filePath) {
          File file = new File(filePath);
          boolean executable = file.setExecutable(true);
          System.out.println(filePath + ", executable? " + executable);
          boolean readable = file.setReadable(true);
          System.out.println(filePath + ", readable? " + readable);
          boolean writeable = file.setWritable(true);
          System.out.println(filePath + ", writeable? " + writeable);
     }

     public static void deleteFileIfExists(String filePath) {
          try {
               Files.deleteIfExists(new File(filePath).toPath());
          } catch (IOException | InvalidPathException e) {
               System.out.println("Unable to delete existing File @ " + filePath + ". " + e.toString());
          }
     }
}
