package com.thrift.common;

import org.springframework.core.io.ClassPathResource;

import java.io.*;

/**
 * @author huangyongwen
 * @date 2021/11/24
 * @Description
 */
public class FileCapacity {

    public String getFileCapacity(long capacity) {
        try {

            ClassPathResource resource = new ClassPathResource("Hello.txt");
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


}
