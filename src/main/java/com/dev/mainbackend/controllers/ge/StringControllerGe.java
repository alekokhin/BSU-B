package com.dev.mainbackend.controllers.ge;


import com.dev.mainbackend.models.ge.StringsGe;
import com.dev.mainbackend.request.StringsRequests;
import com.dev.mainbackend.security.services.ImageService;
import com.dev.mainbackend.security.services.ge.StringsServiceGe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/ge/string")
public class StringControllerGe {
    @Autowired
    private StringsServiceGe stringsServiceGe;
    @Autowired
    private ImageService imageService;
    private final Logger logger = LoggerFactory.getLogger(StringControllerGe.class);

    //get requests
    @GetMapping("/all")
    public List<StringsGe> getAllStrings() {
        List<StringsGe> strings = stringsServiceGe.getAllStrings();
        for (StringsGe stringsGe : strings) {
            List<String> allImages = stringsGe.getImages();
            if (!allImages.isEmpty()) {
                stringsGe.setImages(List.of(allImages.get(0)));
            }
        }
        return strings;
    }

    @GetMapping("/{id}")
    public StringsGe getStringById(@PathVariable String id) {
        return stringsServiceGe.getStringById(id).orElse(null);
    }

    //post requests

    @PostMapping("/add-string")
    public ResponseEntity<Object> addString(StringsRequests stringsRequests) {
        try {
            // Save the string to MongoDB
            StringsGe stringsGe = new StringsGe(stringsRequests);
            stringsGe.setImages(imageService.saveImages(stringsRequests.getNewImages()));
            stringsServiceGe.saveStrings(stringsGe);
            logger.info("add string for: " + stringsRequests.getTitle());
            return new ResponseEntity<>(stringsGe, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("error during add string: " + stringsRequests.getTitle() + "-" + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }


    @GetMapping("/images/{fileName:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String fileName) throws IOException {
        Resource resource = imageService.getImageResource(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/png") // Set the appropriate content type
                .body(resource);
    }


    //put requests
    @PutMapping("/edit-string/{id}")
    public ResponseEntity<Object> updateString(
            @PathVariable String id,
            @RequestBody StringsRequests stringsRequests
    ) {
        try {


            StringsGe existingString = stringsServiceGe.getStringById(id).orElse(null);
            if (existingString != null) {
                // Your two lists of images
                List<String> oldImages = existingString.getImages();

                List<String> newImages = stringsRequests.getImages();

                // Convert lists to ArrayLists
                ArrayList<String> oldImagesList = new ArrayList<>(oldImages);
                ArrayList<String> newImagesList = new ArrayList<>(newImages);

                // Remove elements of newImagesList from oldImagesList
                oldImagesList.removeAll(newImagesList);
                imageService.deleteImages(oldImagesList);


                // Convert the result back to a list

                if (existingString.getNewImages() != null && !existingString.getNewImages().isEmpty()) {
                    for (MultipartFile image : existingString.getNewImages()) {
                        String imagePath = imageService.saveImage(image);
                        newImages.add(imagePath);
                    }
                }
                StringsGe stringsGe = new StringsGe(stringsRequests);
                stringsGe.setImages(newImages);
                stringsRequests.setImages(newImages);
                stringsRequests.setNewImages(null);

                stringsServiceGe.updateStrings(id, stringsRequests);
            }
            logger.info("successfully edit string: " + id);

            return new ResponseEntity<>(existingString, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("error during edit string: " + id + "-" + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //delete requests
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteString(@PathVariable String id) {
        boolean isDeleted = false;
        StringsGe stringsGe = stringsServiceGe.getStringById(id).orElse(null);
        if (stringsGe != null) {
            // Delete images associated with the string
            List<String> images = stringsGe.getImages();
            try {
                imageService.deleteImages(images);
                isDeleted = stringsServiceGe.deleteStrings(id);
                logger.info("delete string: " + id);
            } catch (Exception e) {
                // Handle exception as needed
                logger.error("error during delete string: " + id);
            }
        }

        return new ResponseEntity<>(isDeleted, (isDeleted ? HttpStatus.OK : HttpStatus.NOT_FOUND));
    }
}
