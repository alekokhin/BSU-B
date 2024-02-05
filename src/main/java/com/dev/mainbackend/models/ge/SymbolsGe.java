package com.dev.mainbackend.models.ge;

import com.dev.mainbackend.request.SymbolsRequests;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "symbols-ge")
public class SymbolsGe {
    @Id
    private String id;
    private String title;
    private String description;
    private String connection;
    private List<String> images;
    private List<MultipartFile> newImages = new ArrayList<>();


    public SymbolsGe(String title, String description, String connection,List<String> images,List<MultipartFile> newImages) {
        this.title = title;
        this.description = description;
        this.connection = connection;
        this.images = images;
        this.newImages = newImages;


    }

    public SymbolsGe() {
    }

    public SymbolsGe(SymbolsRequests symbolsRequests) {
        copyProperties(symbolsRequests);
        setNewImages(null);

    }

    public void copyProperties(SymbolsRequests symbolsRequests) {
        BeanUtils.copyProperties(symbolsRequests, this);
    }

}
