package com.dev.mainbackend.security.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${image.upload.directory}")
    private String uploadDirectory;
    private final Logger logger = LoggerFactory.getLogger(ImageService.class);

    public List<String> saveImages(List<MultipartFile> images) {
        List<String> imagePaths = new ArrayList<>();
        for (MultipartFile image : images) {
            String imagePath = saveImage(image);
            imagePaths.add(imagePath);
        }
        return imagePaths;
    }

    public String saveImage(MultipartFile image) {
        try {


            String originalFileName = image.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString().replace("-", "") + fileExtension;
            String filePath = uploadDirectory + "/" + fileName;

            image.transferTo(new File(filePath));

            return fileName;
        } catch (Exception e) {
            logger.error("file not found: " + e.getMessage());
            return "";
        }
    }

    public List<Resource> getImageResources(List<String> fileNames) throws MalformedURLException {
        List<Resource> resources = new ArrayList<>();
        for (String fileName : fileNames) {
            resources.add(getImageResource(fileName));
        }
        return resources;
    }

    public Resource getImageResource(String fileName) {
        try {
            Path imagePath = Paths.get(uploadDirectory).resolve(fileName);
            return new UrlResource(imagePath.toUri());
        } catch (Exception e) {
            logger.error("file not found: " + e.getMessage());

            return null;
        }

    }

    public void deleteImages(List<String> fileNames) {
        for (String fileName : fileNames) {
            deleteImage(fileName);
        }


    }

    public void deleteImage(String fileName) {
        try {

            Path imagePath = Paths.get(uploadDirectory).resolve(fileName);
            Files.deleteIfExists(imagePath);
        } catch (Exception e) {
            logger.error("deleted file not found");
        }
    }
}