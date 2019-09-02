package com.example.auctionapp.util;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Haidy on  2018-11-26
 * 文件上传工具类服务方法
 **/
public class FileUtil {
    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception{

        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
    }
}
