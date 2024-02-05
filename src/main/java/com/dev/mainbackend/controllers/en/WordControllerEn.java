package com.dev.mainbackend.controllers.en;

import com.dev.mainbackend.models.en.WordsEn;
import com.dev.mainbackend.request.WordRequest;
import com.dev.mainbackend.security.services.en.WordServiceEn;
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
@RequestMapping("/api/en/word")
public class WordControllerEn {

    @Autowired
    private WordServiceEn wordServiceEn;
    private final Logger logger = LoggerFactory.getLogger(WordControllerEn.class);


    //get requests
    @GetMapping("/all")
    public List<WordsEn> getAllWords() {
        return wordServiceEn.getAllWords();
    }

    @GetMapping("/{id}")
    public WordsEn getWordById(@PathVariable String id) {
        return wordServiceEn.getWordById(id).orElse(null);
    }


    //post requests

    @PostMapping("/add-word")
    public WordsEn addWord(@RequestBody WordRequest wordRequest) {
        WordsEn wordsEn = new WordsEn(wordRequest);
        logger.info("add word: " + wordRequest.getWord());
        return wordServiceEn.saveWords(wordsEn);
    }

    //put requests
    @PutMapping("/edit-word/{id}")
    public ResponseEntity<WordsEn> updateWord(
            @PathVariable String id,
            @RequestBody WordRequest wordRequest
    ) {
        try {
            Optional<WordsEn> updatedWordOptional = wordServiceEn.updateWords(id, wordRequest);
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
            isDeleted = wordServiceEn.deleteWords(id);
            logger.info("delete word: " + id);
        } catch (Exception e) {
            logger.error("error during delete word: " + id + "-" + e.getMessage());
        }
        return new ResponseEntity<>(isDeleted, (isDeleted ? HttpStatus.OK : HttpStatus.NOT_FOUND));
    }
}
