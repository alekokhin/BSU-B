package com.dev.mainbackend.security.services.en;

import com.dev.mainbackend.models.en.AnalyzedTextEn;
import com.dev.mainbackend.repository.en.AnalyzedTextRepositoryEn;
import com.dev.mainbackend.request.AnalyzedTextRequests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnalyzedTextServiceEn {
    private final AnalyzedTextRepositoryEn analyzedTextRepository;

    @Autowired
    public AnalyzedTextServiceEn(AnalyzedTextRepositoryEn analyzedTextRepository) {
        this.analyzedTextRepository = analyzedTextRepository;
    }

    public AnalyzedTextEn saveAnalyzedText(AnalyzedTextEn analyzedTextEn) {
        return analyzedTextRepository.save(analyzedTextEn);
    }

    public List<AnalyzedTextEn> getAllAnalyzedText() {
        return analyzedTextRepository.findAll();
    }

    public Optional<AnalyzedTextEn> getAnalyzedTextById(String id) {
        try {
            return analyzedTextRepository.findById(id);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<AnalyzedTextEn> updateAnalyzedText(String id, AnalyzedTextRequests analyzedTextRequests) {
        Optional<AnalyzedTextEn> existingItemOptional = analyzedTextRepository.findById(id);
        if (existingItemOptional.isPresent()) {
            AnalyzedTextEn existingAnalyzedTextEn = existingItemOptional.get();
            existingAnalyzedTextEn.copyProperties(analyzedTextRequests);
            return Optional.of(analyzedTextRepository.save(existingAnalyzedTextEn));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteAnalyzedText(String id) {
        Optional<AnalyzedTextEn> existingItemOptional = analyzedTextRepository.findById(id);
        if (existingItemOptional.isPresent()) {
            analyzedTextRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
