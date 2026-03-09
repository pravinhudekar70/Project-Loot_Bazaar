package com.ecom.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile image) throws IOException {
        String originalFileName = image.getOriginalFilename();

        if (originalFileName == null || !originalFileName.contains(".")) {
            throw new RuntimeException("Invalid file name");
        }

        String randomId = UUID.randomUUID().toString();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = randomId + extension;

        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String filePath = path + File.separator + fileName;

        Files.copy(image.getInputStream(), Paths.get(filePath));

        return fileName;
    }
}
