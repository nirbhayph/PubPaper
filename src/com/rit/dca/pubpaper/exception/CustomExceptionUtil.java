package com.rit.dca.pubpaper.exception;

public class CustomExceptionUtil {
        public static String[] getCaller(Exception e) {
            StackTraceElement[] elements = e.getStackTrace();

            String callerMethodName = elements[1].getMethodName();
            String callerClassName = elements[1].getClassName();
            String callerMethodLineNumber = Integer.toString(elements[1].getLineNumber());

            return new String[]{callerClassName, callerMethodName, callerMethodLineNumber};
        }
}
