package com.asset.fc.sp.rest.data.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author nour.ihab
 */
public class FcLogger {

    public static final Logger business = LogManager.getLogger("businessLogger");
    private static final Logger exception = LogManager.getLogger("exceptionLogger");

    public static final void logException(String message) {
        business.error(message);
        exception.error(message);
    }

    public static final void logException(String message, Exception ex) {
        business.error(message);
        exception.error(message, ex);
    }

    public static final void logException(Exception ex) {
        business.error(ex.getMessage());
        exception.error(ex.getMessage(), ex);
    }

    public static final void logThrowable(Throwable th) {
        business.error(th.getMessage());
        exception.error(th.getMessage(), th);
    }

}
