package com.dev.mainbackend.security.services.en;

import com.dev.mainbackend.models.en.StringsEn;
import com.dev.mainbackend.repository.en.StringRepository;
import com.dev.mainbackend.request.StringsRequests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class StringsServiceEn {
    private final StringRepository stringRepository;

    @Autowired
    public StringsServiceEn(StringRepository stringRepository) {
        this.stringRepository = stringRepository;
    }

    public StringsEn saveStrings(StringsEn stringsEn) {
        return stringRepository.save(stringsEn);
    }

    public List<StringsEn> getAllStrings() {
        return stringRepository.findAll();
    }

    public Optional<StringsEn> getStringById(String id) {
        return stringRepository.findById(id);
    }

    public Optional<StringsEn> updateStrings(String id, StringsRequests stringsRequests) throws IOException {
        Optional<StringsEn> existingItemOptional = stringRepository.findById(id);

        if (existingItemOptional.isPresent()) {
            StringsEn existingItem = existingItemOptional.get();
            existingItem.copyProperties(stringsRequests);
            return Optional.of(stringRepository.save(existingItem));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteStrings(String id) {
        Optional<StringsEn> itemOptional = stringRepository.findById(id);

        if (itemOptional.isPresent()) {
            stringRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
