package com.leyou.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.service.ImageUploadService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {


    @Autowired
    private FastFileStorageClient storageClient;

    public static final List<String> CONTENT_TYPE= Arrays.asList("image/jpeg", "image/gif");

    @Override
    public String upload(MultipartFile file) {


        if(!CONTENT_TYPE.contains(file.getContentType())){
            return null;
        }
        try {
            BufferedImage bufferedImage  = ImageIO.read(file.getInputStream());
            if (bufferedImage==null) return null;
            String s = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            StorePath storePath = this.storageClient.uploadFile(
                    file.getInputStream(), file.getSize(), s, null);
             return "http://image.leyou.com/"+storePath.getFullPath();

        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
