package com.dev.mainbackend.controllers.ge;

import com.dev.mainbackend.models.ge.AnalyzedTextGe;
import com.dev.mainbackend.request.AnalyzedTextRequests;
import com.dev.mainbackend.security.services.ge.AnalyzedTextServiceGe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/ge/analyzedText")
public class AnalyzedTextControllerGe {
    @Autowired
    private AnalyzedTextServiceGe analyzedTextServiceGe;
    private final Logger logger = LoggerFactory.getLogger(AnalyzedTextControllerGe.class);


    //get requests
    @GetMapping("/all")
    public List<AnalyzedTextGe> getAllAnalyzedTexts() {
        return analyzedTextServiceGe.getAllAnalyzedText();
    }

    @GetMapping("/{id}")
    public AnalyzedTextGe getAnalyzedTextById(@PathVariable String id) {
        return analyzedTextServiceGe.getAnalyzedTextById(id).orElse(null);
    }
    //post requests

    @PostMapping("/add-analyzedText")
    public AnalyzedTextGe addAnalyzedText(@RequestBody AnalyzedTextRequests analyzedTextRequests) {
        AnalyzedTextGe analyzedTextGe = new AnalyzedTextGe(analyzedTextRequests);
        logger.info("add text for: " + analyzedTextRequests.getTitle());
        return analyzedTextServiceGe.saveAnalyzedText(analyzedTextGe);
    }

    //put requests
    @PutMapping("/edit-analyzedText/{id}")
    public ResponseEntity<AnalyzedTextGe> updateAnalyzedText(
            @PathVariable String id,
            @RequestBody AnalyzedTextRequests analyzedTextRequests
    ) {
        try {
            Optional<AnalyzedTextGe> updatedAnalyzedTextOptional = analyzedTextServiceGe.updateAnalyzedText(id, analyzedTextRequests);
            logger.info("edit text: " + id);
            return ResponseEntity.ok(updatedAnalyzedTextOptional.get());
        } catch (Exception e) {
            logger.error("error during edit text: " + id + "-" + e.getMessage());
            return ResponseEntity.badRequest().build();
        }

    }


    //delete requests
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAnalyzedText(@PathVariable String id) {
        boolean isDeleted = false;
        try {
            isDeleted = analyzedTextServiceGe.deleteAnalyzedText(id);
            logger.info("delete text: " + id);
        } catch (Exception e) {
            logger.error("error during delete text: " + id + "-" + e.getMessage());
        }
        return new ResponseEntity<>(isDeleted, (isDeleted ? HttpStatus.OK : HttpStatus.NOT_FOUND));


    }
}
