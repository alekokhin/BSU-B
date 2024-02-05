package com.dev.mainbackend.security.services.ge;

import com.dev.mainbackend.models.ge.WordsGe;
import com.dev.mainbackend.repository.ge.WordRepositoryGe;
import com.dev.mainbackend.request.WordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class WordServiceGe {
    private final WordRepositoryGe wordRepositoryGe;

    @Autowired
    public WordServiceGe(WordRepositoryGe wordRepositoryGe) {
        this.wordRepositoryGe = wordRepositoryGe;
    }


    public WordsGe saveWords(WordsGe wordsGe) {
        return wordRepositoryGe.save(wordsGe);
    }

    public List<WordsGe> getAllWords() {
        return wordRepositoryGe.findAll();
    }

    public Optional<WordsGe> getWordById(String id) {
        return wordRepositoryGe.findById(id);
    }

    public Optional<WordsGe> updateWords(String id, WordRequest wordRequests) throws IOException {
        Optional<WordsGe> existingItemOptional = wordRepositoryGe.findById(id);

        if (existingItemOptional.isPresent()) {
            WordsGe existingItem = existingItemOptional.get();
            existingItem.copyProperties(wordRequests);
            return Optional.of(wordRepositoryGe.save(existingItem));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteWords(String id) {
        Optional<WordsGe> itemOptional = wordRepositoryGe.findById(id);

        if (itemOptional.isPresent()) {
            wordRepositoryGe.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
