package com.dev.mainbackend.controllers.en;

import com.dev.mainbackend.controllers.ge.AnalyzedTextControllerGe;
import com.dev.mainbackend.models.en.AnalyzedTextEn;
import com.dev.mainbackend.request.AnalyzedTextRequests;
import com.dev.mainbackend.security.services.en.AnalyzedTextServiceEn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/en/analyzedText")
public class AnalyzedTextControllerEn {
    @Autowired
    private AnalyzedTextServiceEn analyzedTextServiceEn;

    private final Logger logger = LoggerFactory.getLogger(AnalyzedTextControllerEn.class);

    //get requests
    @GetMapping("/all")
    public List<AnalyzedTextEn> getAllAnalyzedTexts() {
        return analyzedTextServiceEn.getAllAnalyzedText();
    }

    @GetMapping("/{id}")
    public AnalyzedTextEn getAnalyzedTextById(@PathVariable String id) {
        return analyzedTextServiceEn.getAnalyzedTextById(id).orElse(null);
    }
    //post requests

    @PostMapping("/add-analyzedText")
    public AnalyzedTextEn addAnalyzedText(@RequestBody AnalyzedTextRequests analyzedTextRequests)  {
        AnalyzedTextEn analyzedTextEn = new AnalyzedTextEn(analyzedTextRequests);
        logger.info("add text for: " + analyzedTextRequests.getTitle());
        return analyzedTextServiceEn.saveAnalyzedText(analyzedTextEn);
    }
    //put requests
    @PutMapping("/edit-analyzedText/{id}")
    public ResponseEntity<AnalyzedTextEn> updateAnalyzedText(
            @PathVariable String id,
            @RequestBody AnalyzedTextRequests analyzedTextRequests
    ) {
        try{


        Optional<AnalyzedTextEn> updatedAnalyzedTextOptional = analyzedTextServiceEn.updateAnalyzedText(id, analyzedTextRequests);
            logger.info("edit text: " + id);

            return ResponseEntity.ok(updatedAnalyzedTextOptional.get());
        }catch (Exception e){
            logger.error("error during edit text: " + id + "-" + e.getMessage());

            return ResponseEntity.notFound().build();

        }

    }


    //delete requests
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAnalyzedText(@PathVariable String id) {
        boolean isDeleted = false;
        try {
            isDeleted = analyzedTextServiceEn.deleteAnalyzedText(id);
            logger.info("delete text: " + id);
        } catch (Exception e) {
            logger.error("error during delete text: " + id + "-" + e.getMessage());
        }
        return new ResponseEntity<>(isDeleted, (isDeleted ? HttpStatus.OK : HttpStatus.NOT_FOUND));

    }
}
