package com.dev.mainbackend.controllers.en;

import com.dev.mainbackend.models.en.SymbolsEn;
import com.dev.mainbackend.request.SymbolsRequests;
import com.dev.mainbackend.security.services.ImageService;
import com.dev.mainbackend.security.services.en.SymbolsServiceEn;
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
@RequestMapping("/api/en/symbol")
public class SymbolControllerEn {

    @Autowired
    private SymbolsServiceEn symbolsServiceEn;
    @Autowired
    private ImageService imageService;
    private final Logger logger = LoggerFactory.getLogger(SymbolControllerEn.class);


    //get requests
    @GetMapping("/all")
    public List<SymbolsEn> getAllSymbols() {
        List<SymbolsEn> symbols = symbolsServiceEn.getAllSymbols();
        for (SymbolsEn symbolsEn : symbols) {
            List<String> allImages = symbolsEn.getImages();
            if (!allImages.isEmpty()) {
                symbolsEn.setImages(List.of(allImages.get(0)));
            }
        }
        return symbolsServiceEn.getAllSymbols();
    }

    @GetMapping("/{id}")
    public SymbolsEn getSymbolById(@PathVariable String id) {
        return symbolsServiceEn.getSymbolById(id).orElse(null);
    }


    //post requests

    @PostMapping("/add-symbol")
    public ResponseEntity<Object> addSymbol(SymbolsRequests symbolsRequests) {
        try {
            SymbolsEn symbolsEn = new SymbolsEn(symbolsRequests);
            symbolsEn.setImages(imageService.saveImages(symbolsRequests.getNewImages()));
            logger.info("add symbols: " + symbolsRequests.getTitle());
            return new ResponseEntity<>(symbolsEn, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("error during add symbol: " + symbolsRequests.getTitle() + "-" + e.getMessage());
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
    @PutMapping("/edit-symbol/{id}")
    public ResponseEntity<SymbolsEn> updateSymbol(
            @PathVariable String id,
            @RequestBody SymbolsRequests symbolsRequests
    ) {
        try {


            SymbolsEn existingSymbol = symbolsServiceEn.getSymbolById(id).orElse(null);
            if (existingSymbol != null) {
                // Your two lists of images
                List<String> oldImages = existingSymbol.getImages();

                List<String> newImages = symbolsRequests.getImages();

                // Convert lists to ArrayLists
                ArrayList<String> oldImagesList = new ArrayList<>(oldImages);
                ArrayList<String> newImagesList = new ArrayList<>(newImages);

                // Remove elements of newImagesList from oldImagesList
                oldImagesList.removeAll(newImagesList);
                imageService.deleteImages(oldImagesList);


                // Convert the result back to a list

                if (existingSymbol.getNewImages() != null && !existingSymbol.getNewImages().isEmpty()) {
                    for (MultipartFile image : existingSymbol.getNewImages()) {
                        String imagePath = imageService.saveImage(image);
                        newImages.add(imagePath);
                    }
                }
                SymbolsEn symbolsEn = new SymbolsEn(symbolsRequests);
                symbolsEn.setImages(newImages);
                symbolsRequests.setImages(newImages);
                symbolsRequests.setNewImages(null);

                symbolsServiceEn.updateSymbols(id, symbolsRequests);
            }
            logger.info("edit symbol: " + id);
            return new ResponseEntity<>(existingSymbol, HttpStatus.OK);


        } catch (Exception e) {
            logger.error("error during editing symbol: " + id);

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //delete requests
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSymbol(@PathVariable String id) {
        boolean isDeleted = false;
        SymbolsEn symbolsEn = symbolsServiceEn.getSymbolById(id).orElse(null);
        if (symbolsEn != null) {
            // Delete images associated with the symbol
            List<String> images = symbolsEn.getImages();
            try {
                imageService.deleteImages(images);
                isDeleted = symbolsServiceEn.deleteSymbols(id);
                logger.info("delete symbol: " + id);
            } catch (Exception e) {
                logger.error("error during delete symbol: " + id);
            }
        }

        return new ResponseEntity<>(isDeleted, (isDeleted ? HttpStatus.OK : HttpStatus.NOT_FOUND));
    }

}

