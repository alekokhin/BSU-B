package com.dev.mainbackend.security.services.ge;

import com.dev.mainbackend.models.ge.StringsGe;
import com.dev.mainbackend.repository.ge.StringRepositoryGe;
import com.dev.mainbackend.request.StringsRequests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class StringsServiceGe {
    private final StringRepositoryGe stringRepositoryGe;

    @Autowired
    public StringsServiceGe(StringRepositoryGe stringRepositoryGe) {
        this.stringRepositoryGe = stringRepositoryGe;
    }

    public StringsGe saveStrings(StringsGe stringsGe) {
        return stringRepositoryGe.save(stringsGe);
    }

    public List<StringsGe> getAllStrings() {
        return stringRepositoryGe.findAll();
    }

    public Optional<StringsGe> getStringById(String id) {
        return stringRepositoryGe.findById(id);
    }

    public Optional<StringsGe> updateStrings(String id, StringsRequests stringsRequests) throws IOException {
        Optional<StringsGe> existingItemOptional = stringRepositoryGe.findById(id);

        if (existingItemOptional.isPresent()) {
            StringsGe existingItem = existingItemOptional.get();
            existingItem.copyProperties(stringsRequests);
            return Optional.of(stringRepositoryGe.save(existingItem));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteStrings(String id) {
        Optional<StringsGe> itemOptional = stringRepositoryGe.findById(id);

        if (itemOptional.isPresent()) {
            stringRepositoryGe.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
