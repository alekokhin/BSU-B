package com.dev.mainbackend.security.services.ge;

import com.dev.mainbackend.models.ge.SymbolsGe;
import com.dev.mainbackend.repository.ge.SymbolRepositoryGe;
import com.dev.mainbackend.request.SymbolsRequests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class SymbolsServiceGe {
    private final SymbolRepositoryGe symbolRepositoryGe;

    @Autowired
    public SymbolsServiceGe(SymbolRepositoryGe symbolRepositoryGe) {
        this.symbolRepositoryGe = symbolRepositoryGe;
    }

    public SymbolsGe saveSymbols(SymbolsGe symbolsGe) {
        return symbolRepositoryGe.save(symbolsGe);
    }
    public List<SymbolsGe> getAllSymbols(){
        return symbolRepositoryGe.findAll();
    }
    public Optional<SymbolsGe> getSymbolById(String id) {
        return symbolRepositoryGe.findById(id);
    }
    public Optional<SymbolsGe> updateSymbols(String id, SymbolsRequests symbolsRequests) throws IOException {
        Optional<SymbolsGe> existingItemOptional = symbolRepositoryGe.findById(id);

        if (existingItemOptional.isPresent()) {
            SymbolsGe existingItem = existingItemOptional.get();
            existingItem.copyProperties(symbolsRequests);
            return Optional.of(symbolRepositoryGe.save(existingItem));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteSymbols(String id) {
        Optional<SymbolsGe> itemOptional = symbolRepositoryGe.findById(id);

        if (itemOptional.isPresent()) {
            symbolRepositoryGe.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
