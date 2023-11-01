package com.dubbo.util;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @Author 码农阿雨 | WebSite📶 : https://learnjava.baimuxym.cn/
 * @Date 2023/10/13 17:25
 * @Description
 **/
public class ClassCopyUtils {
    static Pattern pattern = Pattern.compile("ClassCopyUtils(\\d+).java");

    public static void copyFile(String inputFilePath, String inputFileName, String outPutFilePath) throws IOException {

        String outPutFileName = getOutPutFileName(inputFilePath, inputFileName, outPutFilePath);

        InputStream inputStream = new FileInputStream(inputFilePath + "\\" + inputFileName);

        // 使用OutputStream将数据写入新文件
        OutputStream outputStream = new FileOutputStream(outPutFileName);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        // 关闭输入和输出流
        inputStream.close();
        outputStream.close();


    }

    public String getOutPutFileName(String inputFilePath, String inputFileName, String outputFilePath) throws FileNotFoundException {

        File file = new File(inputFilePath + "\\" + inputFileName);
        if (!file.exists()) {
            throw new FileNotFoundException(inputFilePath + "\\" + inputFileName + "目标文件不存在");
        }

        File file2 = new File(outputFilePath);
        if (!file2.exists() || !file2.isDirectory()) {
            throw new FileNotFoundException(file2.getPath() + "\\" + file.getName() + "输出目录不存在");
        }

        String fileName = file.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);


        List<File> dirAllFile = getDirAllFile(outputFilePath);


        int maxNum = 0;
        String maxFileName = "";
        for (File existingFile : dirAllFile) {

            Matcher matcher = pattern.matcher(existingFile.getName());

            // 查找并输出匹配到的数字
            while (matcher.find()) {
                String matchedNumber = matcher.group(1);
                int number = Integer.parseInt(matchedNumber);
                if (number > maxNum) {
                    maxNum = number;
                    maxFileName = existingFile.getName();
                }
            }
        }

        System.out.println("查找到当前最大序号的文件：" + maxFileName);

        Date createTime = getFileCreateTime(inputFilePath + "\\" + maxFileName);
        // 将日期转换为LocalDate
        assert createTime != null;
        LocalDate localDate1 = createTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (localDate1.isEqual(localDate2)) {
            System.out.println("当天已经创建过文件了，跳过提交");
            return "";
        }
        maxNum = maxNum + 1;
        return outputFilePath + "\\" + inputFileName.substring(0, fileName.lastIndexOf(".")) + maxNum + "." + fileExtension;

    }

    // 比较两个日期的年月日部分
    public static boolean isSameDate(Date date1, Date date2) {
        java.util.Calendar cal1 = java.util.Calendar.getInstance();
        cal1.setTime(date1);
        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        cal2.setTime(date2);

        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
                cal1.get(java.util.Calendar.MONTH) == cal2.get(java.util.Calendar.MONTH) &&
                cal1.get(java.util.Calendar.DAY_OF_MONTH) == cal2.get(java.util.Calendar.DAY_OF_MONTH);
    }


    public static List<File> getDirAllFile(String directoryPath) {
        List<File> fileNames = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
            paths.filter(path -> Files.isRegularFile(path)).forEach(path -> {
                fileNames.add(path.getFileName().toFile());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileNames;
    }


    public static void main(String[] args) throws IOException {

        String inputFileDir = "F:\\开发任务\\Dubbo\\dubbo-samples-test\\dubbo-samples-springboot\\dubbo-samples-springboot-provider\\src\\main\\java\\com\\dubbo\\util";

        String outputFileDir = "F:\\开发任务\\Dubbo\\dubbo-samples-test\\dubbo-samples-springboot\\dubbo-samples-springboot-provider\\src\\main\\resources\\file";

        String copyInputFileName = "ClassCopyUtils.java";


        copyFile(inputFileDir, copyInputFileName, outputFileDir);




    }


    private Date getFileCreateTime(String fileName) {
        try {
            Path path = Paths.get(fileName);

            // 获取文件的基本属性
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);

            // 获取文件的创建时间
            FileTime creationTime = attributes.creationTime();
            Date createdDate = new Date(creationTime.toMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("创建时间: " + dateFormat.format(createdDate));

            return createdDate;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
