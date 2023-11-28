package com.hellocoder;

import org.junit.Test;

/**
 * @Author huangyongwen
 * @Date 2023/10/13 17:19
 * @Description
 **/

public class Test1 {
    @Test
    public void mainLineTest() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length > 0) {
            StackTraceElement element = stackTrace[1];
            System.out.println("File: " + element.getFileName());
            System.out.println("Line: " + element.getLineNumber());
        }
    }
}
