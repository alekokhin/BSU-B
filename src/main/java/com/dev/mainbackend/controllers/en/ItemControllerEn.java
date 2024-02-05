package com.dev.mainbackend.controllers.en;

import com.dev.mainbackend.models.en.ItemEn;
import com.dev.mainbackend.request.ItemsRequests;
import com.dev.mainbackend.security.services.ImageService;
import com.dev.mainbackend.security.services.en.ItemServiceEn;
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
@RequestMapping("/api/en/item")
public class ItemControllerEn {

    @Autowired
    private ItemServiceEn itemServiceEn;
    @Autowired
    private ImageService imageService;

    private final Logger logger = LoggerFactory.getLogger(ItemControllerEn.class);


    //get requests
    @GetMapping("/all")
    public List<ItemEn> getAllItems() {
        List<ItemEn> items = itemServiceEn.getAllItems();
        for (ItemEn itemEn : items) {
            List<String> allImages = itemEn.getImages();
            if (!allImages.isEmpty()) {
                itemEn.setImages(List.of(allImages.get(0)));
            }
        }
        return items;
    }

    @GetMapping("/{itemId}")
    public ItemEn getItemById(@PathVariable String itemId) {
        return itemServiceEn.getItemById(itemId).orElse(null);
    }


    //post requests

    @PostMapping("/add-item")
    public ResponseEntity<Object> addItem(ItemsRequests addItemRequest) {
        try {
            // Create an item object and set the image paths
            ItemEn itemEn = new ItemEn(addItemRequest);
            itemEn.setImages(imageService.saveImages(addItemRequest.getNewImages()));
            // Save the item to MongoDB
            itemServiceEn.saveItem(itemEn);
            logger.info("add item " + addItemRequest.getTitle());
            return new ResponseEntity<>(itemEn, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("error during add item: " + addItemRequest.getTitle() + "-" + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
    }

    //put requests
    @PutMapping("/edit-item/{itemId}")
    public ResponseEntity<Object> updateItem(
            @PathVariable String itemId,
            ItemsRequests itemsRequests
    ) {
        try {


            ItemEn existingItem = itemServiceEn.getItemById(itemId).orElse(null);
            if (existingItem != null) {
                // Your two lists of images
                List<String> oldImages = existingItem.getImages();

                List<String> newImages = itemsRequests.getImages();

                // Convert lists to ArrayLists
                ArrayList<String> oldImagesList = new ArrayList<>(oldImages);
                ArrayList<String> newImagesList = new ArrayList<>(newImages);

                // Remove elements of newImagesList from oldImagesList
                oldImagesList.removeAll(newImagesList);
                imageService.deleteImages(oldImagesList);


                // Convert the result back to a list

                if (itemsRequests.getNewImages() != null && !itemsRequests.getNewImages().isEmpty()) {
                    for (MultipartFile image : itemsRequests.getNewImages()) {
                        String imagePath = imageService.saveImage(image);
                        newImages.add(imagePath);
                    }
                }
                ItemEn itemEn = new ItemEn(itemsRequests);
                itemEn.setImages(newImages);
                itemsRequests.setImages(newImages);
                itemsRequests.setNewImages(null);
                itemServiceEn.updateItem(itemId, itemsRequests);
            }
            logger.info("successfully edit item: " + itemId);

            return new ResponseEntity<>(existingItem, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("error during edit: " + e.getMessage());
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
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@PathVariable String itemId) {
        boolean isDeleted = false;
        ItemEn item = itemServiceEn.getItemById(itemId).orElse(null);
        if (item != null) {
            // Delete images associated with the item
            List<String> images = item.getImages();
            try {
                imageService.deleteImages(images);
                isDeleted = itemServiceEn.deleteItem(itemId);
                logger.info("delete item: " + itemId);
            } catch (Exception e) {
                logger.error("error during delete item: " + itemId + "-" + e.getMessage());
            }
        }

        return new ResponseEntity<>(isDeleted, (isDeleted ? HttpStatus.OK : HttpStatus.NOT_FOUND));
    }
}