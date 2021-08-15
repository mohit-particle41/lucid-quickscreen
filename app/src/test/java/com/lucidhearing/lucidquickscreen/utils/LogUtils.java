package com.lucidhearing.lucidquickscreen.utils;

import java.util.logging.Logger;

public class LogUtils {
    public static Logger getLogger() {
        Logger logger = Logger.getLogger(String.valueOf(LogUtils.class));
        return logger;
    }

    public static void info(Logger logger, String message) {
        logger.info(message);
    }
}