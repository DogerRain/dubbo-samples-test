package com.dubbo.common;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.net.URLDecoder;

/**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
 * @site
 * @date 2021/11/24
 * @Description
 */
public class FileCapacity {

    public String getFileCapacity(long capacity) {
        try {

//            String fileName = this.getClass().getResource("/Hello.txt").getFile();
            ClassPathResource resource = new ClassPathResource("Hello.txt");
//            String fileName = resource.getFilename();

//            String fileName = this.getClass().getClassLoader().getResource("/Hello.txt").getPath();
//            fileName = URLDecoder.decode(fileName, "UTF-8");

//            File file = new File(fileName);
            Long fileLength = capacity;
            byte[] fileContent = new byte[fileLength.intValue()];
            InputStream in = resource.getInputStream();
            in.read(fileContent);
            in.close();
            return new String(fileContent);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static void main(String[] args) throws IOException{
        int k = 100;
        String s = new FileCapacity().getFileCapacity(k*1024);
        System.out.println(s.length());
        File file2 = new File("F:\\HelloCoder_"+k+"K.txt");
        OutputStream os2 = new FileOutputStream(file2,false);
        byte[] bytes = s.getBytes();
        os2.write(bytes);
        os2.close();
    }

}
