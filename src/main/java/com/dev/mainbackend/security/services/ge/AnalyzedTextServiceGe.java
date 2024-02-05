package com.dev.mainbackend.security.services.ge;

import com.dev.mainbackend.models.ge.AnalyzedTextGe;
import com.dev.mainbackend.repository.ge.AnalyzedTextRepositoryGe;
import com.dev.mainbackend.request.AnalyzedTextRequests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnalyzedTextServiceGe {
    private final AnalyzedTextRepositoryGe analyzedTextRepositoryGe;

    @Autowired
    public AnalyzedTextServiceGe(AnalyzedTextRepositoryGe analyzedTextRepositoryGe) {
        this.analyzedTextRepositoryGe = analyzedTextRepositoryGe;
    }

    public AnalyzedTextGe saveAnalyzedText(AnalyzedTextGe analyzedTextGe) {
        return analyzedTextRepositoryGe.save(analyzedTextGe);
    }

    public List<AnalyzedTextGe> getAllAnalyzedText() {
        return analyzedTextRepositoryGe.findAll();
    }

    public Optional<AnalyzedTextGe> getAnalyzedTextById(String id) {
        try {
            return analyzedTextRepositoryGe.findById(id);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<AnalyzedTextGe> updateAnalyzedText(String id, AnalyzedTextRequests analyzedTextRequests) {
        Optional<AnalyzedTextGe> existingItemOptional = analyzedTextRepositoryGe.findById(id);
        if (existingItemOptional.isPresent()) {
            AnalyzedTextGe existingAnalyzedTextGe = existingItemOptional.get();
            existingAnalyzedTextGe.copyProperties(analyzedTextRequests);
            return Optional.of(analyzedTextRepositoryGe.save(existingAnalyzedTextGe));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteAnalyzedText(String id) {
        Optional<AnalyzedTextGe> existingItemOptional = analyzedTextRepositoryGe.findById(id);
        if (existingItemOptional.isPresent()) {
            analyzedTextRepositoryGe.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
