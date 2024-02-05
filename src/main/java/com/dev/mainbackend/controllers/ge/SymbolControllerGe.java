package com.dev.mainbackend.controllers.ge;

import com.dev.mainbackend.models.ge.SymbolsGe;
import com.dev.mainbackend.request.SymbolsRequests;
import com.dev.mainbackend.security.services.ImageService;
import com.dev.mainbackend.security.services.ge.SymbolsServiceGe;
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
@RequestMapping("/api/ge/symbol")
public class SymbolControllerGe {
    @Autowired
    private SymbolsServiceGe symbolsServiceGe;
    @Autowired
    private ImageService imageService;
    private final Logger logger = LoggerFactory.getLogger(SymbolControllerGe.class);


    //get requests
    @GetMapping("/all")
    public List<SymbolsGe> getAllSymbols() {
        List<SymbolsGe> symbols = symbolsServiceGe.getAllSymbols();
        for (SymbolsGe symbolsGe : symbols) {
            List<String> allImages = symbolsGe.getImages();
            if (!allImages.isEmpty()) {
                symbolsGe.setImages(List.of(allImages.get(0)));
            }
        }

        return symbols;
    }

    @GetMapping("/{id}")
    public SymbolsGe getSymbolById(@PathVariable String id) {
        return symbolsServiceGe.getSymbolById(id).orElse(null);
    }


    //post requests

    @PostMapping("/add-symbol")
    public ResponseEntity<Object> addSymbol(SymbolsRequests symbolsRequests) {
        try {
            SymbolsGe symbolsGe = new SymbolsGe(symbolsRequests);
            symbolsGe.setImages(imageService.saveImages(symbolsRequests.getNewImages()));
            logger.info("add symbols: " + symbolsRequests.getTitle());
            return new ResponseEntity<>(symbolsGe, HttpStatus.OK);
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
    public ResponseEntity<SymbolsGe> updateSymbol(
            @PathVariable String id,
            @RequestBody SymbolsRequests symbolsRequests
    ) {
        try {

            SymbolsGe existingSymbol = symbolsServiceGe.getSymbolById(id).orElse(null);
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
                SymbolsGe symbolsGe = new SymbolsGe(symbolsRequests);
                symbolsGe.setImages(newImages);
                symbolsRequests.setImages(newImages);
                symbolsRequests.setNewImages(null);

                symbolsServiceGe.updateSymbols(id, symbolsRequests);
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
        SymbolsGe symbolsGe = symbolsServiceGe.getSymbolById(id).orElse(null);
        if (symbolsGe != null) {
            // Delete images associated with the symbol
            List<String> images = symbolsGe.getImages();
            try {
                imageService.deleteImages(images);
                isDeleted = symbolsServiceGe.deleteSymbols(id);
                logger.info("delete symbol: " + id);
            } catch (Exception e) {
                logger.error("error during delete symbol: " + id);
            }
        }

        return new ResponseEntity<>(isDeleted, (isDeleted ? HttpStatus.OK : HttpStatus.NOT_FOUND));
    }

}
