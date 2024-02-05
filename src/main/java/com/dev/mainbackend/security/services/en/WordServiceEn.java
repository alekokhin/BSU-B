package com.dev.mainbackend.security.services.en;

import com.dev.mainbackend.models.en.WordsEn;
import com.dev.mainbackend.repository.en.WordRepository;
import com.dev.mainbackend.request.WordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class WordServiceEn {
    private final WordRepository wordRepository;

    @Autowired
    public WordServiceEn(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }


    public WordsEn saveWords(WordsEn wordsEn) {
        return wordRepository.save(wordsEn);
    }

    public List<WordsEn> getAllWords() {
        return wordRepository.findAll();
    }

    public Optional<WordsEn> getWordById(String id) {
        return wordRepository.findById(id);
    }

    public Optional<WordsEn> updateWords(String id, WordRequest wordRequests) throws IOException {
        Optional<WordsEn> existingItemOptional = wordRepository.findById(id);

        if (existingItemOptional.isPresent()) {
            WordsEn existingItem = existingItemOptional.get();
            existingItem.copyProperties(wordRequests);
            return Optional.of(wordRepository.save(existingItem));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteWords(String id) {
        Optional<WordsEn> itemOptional = wordRepository.findById(id);

        if (itemOptional.isPresent()) {
            wordRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
