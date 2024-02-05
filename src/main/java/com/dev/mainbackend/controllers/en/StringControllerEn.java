package com.dev.mainbackend.controllers.en;

import com.dev.mainbackend.models.en.StringsEn;
import com.dev.mainbackend.request.StringsRequests;
import com.dev.mainbackend.security.services.ImageService;
import com.dev.mainbackend.security.services.en.StringsServiceEn;
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
@RequestMapping("/api/en/string")
public class StringControllerEn {

    @Autowired
    private StringsServiceEn stringsServiceEn;
    @Autowired
    private ImageService imageService;
    private final Logger logger = LoggerFactory.getLogger(StringControllerEn.class);


    //get requests
    @GetMapping("/all")
    public List<StringsEn> getAllStrings() {
        List<StringsEn> strings = stringsServiceEn.getAllStrings();
        for (StringsEn stringsEn : strings) {
            List<String> allImages = stringsEn.getImages();
            if (!allImages.isEmpty()) {
                stringsEn.setImages(List.of(allImages.get(0)));
            }
        }
        return strings;
    }

    @GetMapping("/{id}")
    public StringsEn getStringById(@PathVariable String id) {
        return stringsServiceEn.getStringById(id).orElse(null);
    }

    //post requests

    @PostMapping("/add-string")
    public ResponseEntity<Object> addString(StringsRequests stringsRequests) {
        try {
            // Save the string to MongoDB
            StringsEn stringsEn = new StringsEn(stringsRequests);
            stringsEn.setImages(imageService.saveImages(stringsRequests.getNewImages()));
            stringsServiceEn.saveStrings(stringsEn);
            logger.info("add string for: " + stringsRequests.getTitle());
            return new ResponseEntity<>(stringsEn, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("error during add string: " + stringsRequests.getTitle() + "-" + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/edit-string/{id}")
    public ResponseEntity<StringsEn> updateString(
            @PathVariable String id,
            @RequestBody StringsRequests stringsRequests
    ) {
        try {


            StringsEn existingString = stringsServiceEn.getStringById(id).orElse(null);
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
                StringsEn stringsEn = new StringsEn(stringsRequests);
                stringsEn.setImages(newImages);
                stringsRequests.setImages(newImages);
                stringsRequests.setNewImages(null);

                stringsServiceEn.updateStrings(id, stringsRequests);
            }
            logger.info("successfully edit string: " + id);


            return new ResponseEntity<>(existingString, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("error during edit string: " + id + "-" + e.getMessage());
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


    //delete requests
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteString(@PathVariable String id) {
        boolean isDeleted = false;
        StringsEn stringsEn = stringsServiceEn.getStringById(id).orElse(null);
        if (stringsEn != null) {
            // Delete images associated with the string
            List<String> images = stringsEn.getImages();
            try {
                imageService.deleteImages(images);
                isDeleted = stringsServiceEn.deleteStrings(id);
                logger.info("delete string: " + id);
            } catch (Exception e) {
                // Handle exception as needed
                logger.error("error during delete string: " + id);
            }
        }

        return new ResponseEntity<>(isDeleted, (isDeleted ? HttpStatus.OK : HttpStatus.NOT_FOUND));
    }
}
