package com.dev.mainbackend.controllers.ge;

import com.dev.mainbackend.models.ge.WordsGe;
import com.dev.mainbackend.request.WordRequest;
import com.dev.mainbackend.security.services.ge.WordServiceGe;
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
@RequestMapping("/api/ge/word")
public class WordControllerGe {

    @Autowired
    private WordServiceGe wordServiceGe;

    private final Logger logger = LoggerFactory.getLogger(WordControllerGe.class);

    //get requests
    @GetMapping("/all")
    public List<WordsGe> getAllWords() {
        return wordServiceGe.getAllWords();
    }

    @GetMapping("/{id}")
    public WordsGe getWordById(@PathVariable String id) {
        return wordServiceGe.getWordById(id).orElse(null);
    }

    //post requests

    @PostMapping("/add-word")
    public WordsGe addWord(@RequestBody WordRequest wordRequest) {
        WordsGe wordsGe = new WordsGe(wordRequest);
        logger.info("add word: " + wordRequest.getWord());
        return wordServiceGe.saveWords(wordsGe);
    }

    //put requests
    @PutMapping("/edit-word/{id}")
    public ResponseEntity<WordsGe> updateWord(
            @PathVariable String id,
            @RequestBody WordRequest wordRequest
    ) {
        try {
            Optional<WordsGe> updatedWordOptional = wordServiceGe.updateWords(id, wordRequest);
            logger.info("edit word: " + id);
            return ResponseEntity.ok(updatedWordOptional.get());
        } catch (Exception e) {
            logger.error("error during editing word: " + id);
            return ResponseEntity.notFound().build();
        }

    }


    //delete requests
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteWord(@PathVariable String id) {
        boolean isDeleted = false;
        try {
            isDeleted = wordServiceGe.deleteWords(id);
            logger.info("delete word: " + id);
        } catch (Exception e) {
            logger.error("error during delete word: " + id + "-" + e.getMessage());
        }
        return new ResponseEntity<>(isDeleted, (isDeleted ? HttpStatus.OK : HttpStatus.NOT_FOUND));
    }
}
