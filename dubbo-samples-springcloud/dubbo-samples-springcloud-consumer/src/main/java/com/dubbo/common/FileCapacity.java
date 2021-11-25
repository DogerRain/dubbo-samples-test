package com.dubbo.common;

import java.io.*;
import java.net.URLDecoder;

/**
 * @author huangyongwen
 * @date 2021/11/24
 * @Description
 */
public class FileCapacity {

    public String getFileCapacity(long capacity) {
        try {

            String fileName = this.getClass().getResource("/Hello.txt").getFile();
            fileName = URLDecoder.decode(fileName, "UTF-8");

            File file = new File(fileName);
            Long fileLength = capacity;
            byte[] fileContent = new byte[fileLength.intValue()];

            FileInputStream in = new FileInputStream(file);
            in.read(fileContent);
            in.close();
            return new String(fileContent);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static void main(String[] args) throws IOException{
        int k = 10;
        String s = new FileCapacity().getFileCapacity(k*1024);

        File file2 = new File("/HelloCoder_"+k+"K.txt");
        OutputStream os2 = new FileOutputStream(file2,false);
        byte[] bytes = s.getBytes();
        os2.write(bytes);
        os2.close();
    }

}
