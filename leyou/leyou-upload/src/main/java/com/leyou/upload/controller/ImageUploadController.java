package com.leyou.upload.controller;

import com.leyou.service.ImageUploadService;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("upload")
public class ImageUploadController {

    @Autowired
    ImageUploadService uploadService;

    @PostMapping("image")
    public ResponseEntity<String> saveImage(MultipartFile file){

        String upload = uploadService.upload(file);
        if(StringUtils.isNotBlank(upload)){
            return ResponseEntity.ok(upload);
        }
        return ResponseEntity.badRequest().build();
    }

}
