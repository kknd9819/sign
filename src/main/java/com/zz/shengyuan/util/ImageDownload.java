package com.zz.shengyuan.util;

import com.zz.shengyuan.constant.SignConstant;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageDownload {

    /**
     * 传入要下载的图片的url列表，将url所对应的图片下载到本地
     * @param serverUrl 服务器地址
     * @param imageName 文件名
     */
    public static void downloadPicture(String serverUrl,String imageName) throws IOException {
        URL url = new URL(serverUrl + imageName);
        DataInputStream dataInputStream = new DataInputStream(url.openStream());
        int i = imageName.lastIndexOf("/");
        //String path = imageName.substring(0, i + 1);
        String fileName = imageName.substring(i + 1, imageName.length());
        File file = new File(SignConstant.DOWNLOAD_PATH);
        if(!file.exists()){
            file.mkdirs();
        }
        File[] files = file.listFiles();
        if(files != null && files.length > 0){
            for(File f:files){
                if(f.getName().equals(fileName)){
                   return;
                }
            }
        }

        Files.copy(dataInputStream, Paths.get(SignConstant.DOWNLOAD_PATH).resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void main(String[] agrs) throws IOException {
        downloadPicture(SignConstant.SERVER_URL,"/upload/image/temp/201712/51383245-328d-49f2-8e72-1598e2e381a3.jpg");
    }
}
