package com.dev.mainbackend.models.en;

import com.dev.mainbackend.request.SymbolsRequests;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "symbols-en")
public class SymbolsEn {
    private String id;
    private String title;
    private String description;
    private String connection;
    private List<String> images;
    private List<MultipartFile> newImages = new ArrayList<>();


    public SymbolsEn(String title, String description, String connection,List<String> images,List<MultipartFile> newImages) {
        this.title = title;
        this.description = description;
        this.connection = connection;
        this.images = images;
        this.newImages = newImages;

    }

    public SymbolsEn() {
    }

    public SymbolsEn(SymbolsRequests symbolsRequests) {
        copyProperties(symbolsRequests);
        setNewImages(null);
    }

    public void copyProperties(SymbolsRequests symbolsRequests) {
        BeanUtils.copyProperties(symbolsRequests, this);
    }

}
