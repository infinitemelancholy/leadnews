package com.leadnews.minio.test;


import io.minio.MinioClient;
import io.minio.PutObjectArgs;

import java.io.FileInputStream;

//@SpringBootTest(classes = MinIOApplication.class)
//@RunWith(SpringRunner.class)
public class MinIOTest {

    /*@Autowired
    private FileStorageService fileStorageService;

    //鎶妉ist.html鏂囦欢涓婁紶鍒癿inio涓紝骞朵笖鍙互鍦ㄦ祻瑙堝櫒涓闂?

    @Test
    public void test() throws FileNotFoundException {
//        FileInputStream fileInputStream = new FileInputStream("D:\\list.html");
//        String path = fileStorageService.uploadHtmlFile("", "list.html", fileInputStream);
        FileInputStream fileInputStream = new FileInputStream("E:\\tmp\\ak47.jpg");
        String path = fileStorageService.uploadImgFile("", "ak47.jpg", fileInputStream);
        System.out.println(path);
    }*/





    /**
     * 鎶妉ist.html鏂囦欢涓婁紶鍒癿inio涓紝骞朵笖鍙互鍦ㄦ祻瑙堝櫒涓闂?
     * @param args
     */
    public static void main(String[] args) {

        try {
            FileInputStream fileInputStream = new FileInputStream("C:\\Users\\yuhon\\Downloads\\index.js");

            //1锛岃幏鍙杕inio鐨勯摼鎺ヤ俊鎭? 鍒涘缓涓€涓猰inio鐨勫鎴风
            MinioClient minioClient = MinioClient.builder().credentials("minio", "minio123").endpoint("http://192.168.200.130:9000").build();

            //2.涓婁紶
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object("plugins/js/index.js") //鏂囦欢鍚嶈瘝
                    .contentType("text/js") //鏂囦欢绫诲瀷
                    .bucket("leadnews") //妗跺悕绉? 涓巑inio绠＄悊鐣岄潰鍒涘缓鐨勬《涓€鑷村嵆鍙?
                    .stream(fileInputStream,fileInputStream.available(),-1).build();
            minioClient.putObject(putObjectArgs);

            //璁块棶璺緞
//            System.out.println("http://192.168.200.130:9000/leadnews/list.html");
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

}
