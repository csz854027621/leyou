package com.leyou.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {

    public String upload(MultipartFile file);
}
