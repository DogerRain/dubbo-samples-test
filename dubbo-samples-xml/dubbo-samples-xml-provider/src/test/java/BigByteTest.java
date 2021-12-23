import org.junit.Test;

import java.io.*;
import java.net.URLDecoder;

/**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
 * @site
 * @date 2021/11/18
 * @Description
 */
public class BigByteTest {
    @Test
    public void calc() throws IOException {
        byte[] bytes = new byte[1024*1000];

        bytes = "get".getBytes();

//        System.out.println(bytes.length);
//
//        System.out.println(this.getClass().getResource("").getPath());
//        System.out.println(this.getClass().getResource(".").getFile());
//        System.out.println(this.getClass().getResource("/").getFile());
//        System.out.println(this.getClass().getClassLoader().getResource(""));

        String fileName = this.getClass().getResource("/HelloCoder.txt").getFile();
        fileName =   URLDecoder.decode(fileName, "UTF-8");
        System.out.println(fileName);
        File file = new File(fileName);
        Long fileLength = file.length();
        byte[] fileContent =  new byte[fileLength.intValue()];

        FileInputStream in = new FileInputStream(file);
        in.read(fileContent);
        in.close();

        System.out.println(fileLength);
        System.out.println(fileContent.length);
        System.out.println(new String(fileContent));


//        useInputStreamCopyFile();

    }
    void useInputStreamCopyFile() throws IOException {
//        File file = new File("F:\\HelloCoder\\Hello1.txt");

//        File file = new File("F:\\HelloCoder.txt");
//        InputStream is = new FileInputStream(file);

        InputStream is = this.getClass().getResourceAsStream("/HelloCoder.txt");




        File file2 = new File("/HelloCoder_50K.txt");
        File file3 = new File("/HelloCoder_100K.txt");
//        if (!file2.exists()){
//            file2.mkdirs();
//        }
//        if (!file3.exists()){
//            file3.mkdirs();
//        }



        OutputStream os2 = new FileOutputStream(file2,false);
        OutputStream os3 = new FileOutputStream(file3,false);
        int len = 0;
        byte[] bytes = new byte[1024];
        while ((len = is.read(bytes)) != -1) {
            if (file2.length()<50*1024){
                os2.write(bytes);
            }
            if (file3.length()<100*1024){
                os3.write(bytes);
            }else {
                break;
            }
        }
        is.close();

        os2.close();
        os3.close();
    }
}
