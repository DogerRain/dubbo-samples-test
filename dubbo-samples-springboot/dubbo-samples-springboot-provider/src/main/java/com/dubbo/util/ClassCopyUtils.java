package com.dubbo.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author huangyongwen
 * @Date 2023/10/13 17:25
 * @Description
 **/
@Slf4j
public class ClassCopyUtils {

    static Pattern pattern = Pattern.compile("\\d+");

    public void copyFile() {

        String sourceFile = "file/ClassCopyUtilsTest.java"; // 源文件名

        String newFileName = "";

        try (InputStream in = ClassCopyUtils.class.getClassLoader().getResourceAsStream(sourceFile);
             OutputStream out = new FileOutputStream(newFileName)) {

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

    @SneakyThrows
    public static String getOutPutFileName(String filePath) {
        ClassLoader classLoader = ClassCopyUtils.class.getClassLoader();
        URL resource = classLoader.getResource(filePath);

        if (resource == null) {
            System.err.println("Resource directory not found.");
        }
        Path directory = Paths.get(resource.toURI());

        List<Path> collect = Files.list(directory).filter(Files::isRegularFile)
                .map(Path::getFileName).collect(Collectors.toList());


        int maxNum = 0;
        for (Path existingFile : collect) {

            // 创建匹配器并进行匹配
            Matcher matcher = pattern.matcher(existingFile.getFileName().toString());

            // 查找并输出匹配到的数字
            while (matcher.find()) {
                String matchedNumber = matcher.group();
                int number = Integer.parseInt(matchedNumber);
                System.out.println("Extracted number: " + number);
                if (number > maxNum) {
                    maxNum = number;
                }
            }
        }

        return "ClassCopyUtilsTest" + maxNum + ".java";


    }


    public static void main(String[] args) {
        System.out.println(new ClassCopyUtils().getOutPutFileName("/file/ClassCopyUtilsTest.java"));
    }
}
