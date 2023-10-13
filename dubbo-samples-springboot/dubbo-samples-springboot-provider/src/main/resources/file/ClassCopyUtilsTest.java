package com.dubbo.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @Author huangyongwen
 * @Date 2023/10/13 17:25
 * @Description
 **/
@Slf4j
public class ClassCopyUtilsTest {
    public void copyFile() {
        String sourcePath = "file/ClassCopyUtilsTest.java"; // 相对路径
        String newFile = "file/ClassCopyUtilsTest1.java"; // 目标文件名

//        String newFile = getClass().getCanonicalName() + "1" + ".java";

        try (InputStream in = new FileInputStream(sourcePath);
             OutputStream out = new FileOutputStream(newFile)) {

            if (in == null) {
                throw new FileNotFoundException("Source file not found.");
            }

            int bytesRead;
            byte[] buffer = new byte[1024];

            // 复制到文件
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            System.out.println("Files copied successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static boolean getAllFiles(String filePath) {
       //目录

    }


    public static void main(String[] args) {
        new ClassCopyUtils().copyFile();
    }
}
