package com.dev.mainbackend.security.services.en;

import com.dev.mainbackend.models.en.SymbolsEn;
import com.dev.mainbackend.repository.en.SymbolRepository;
import com.dev.mainbackend.request.SymbolsRequests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class SymbolsServiceEn {
    private final SymbolRepository symbolRepository;

    @Autowired
    public SymbolsServiceEn(SymbolRepository symbolRepository) {
        this.symbolRepository = symbolRepository;
    }

    public SymbolsEn saveSymbols(SymbolsEn symbolsEn) {
        return symbolRepository.save(symbolsEn);
    }
    public List<SymbolsEn> getAllSymbols(){
        return symbolRepository.findAll();
    }
    public Optional<SymbolsEn> getSymbolById(String id) {
        return symbolRepository.findById(id);
    }
    public Optional<SymbolsEn> updateSymbols(String id, SymbolsRequests symbolsRequests) throws IOException {
        Optional<SymbolsEn> existingItemOptional = symbolRepository.findById(id);

        if (existingItemOptional.isPresent()) {
            SymbolsEn existingItem = existingItemOptional.get();
            existingItem.copyProperties(symbolsRequests);
            return Optional.of(symbolRepository.save(existingItem));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteSymbols(String id) {
        Optional<SymbolsEn> itemOptional = symbolRepository.findById(id);

        if (itemOptional.isPresent()) {
            symbolRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
