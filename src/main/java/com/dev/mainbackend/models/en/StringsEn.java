package com.dev.mainbackend.models.en;

import com.dev.mainbackend.request.StringsRequests;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "strings-en")
public class StringsEn {
    @Id
    private String id;
    private String title;
    private String description;
    private String connection;
    private List<String> images;
    private List<MultipartFile> newImages = new ArrayList<>();


    public StringsEn(String title, String description, String connection, List<String> images,List<MultipartFile> newImages) {
        this.title = title;
        this.description = description;
        this.connection = connection;
        this.images = images;
        this.newImages = newImages;

    }

    public StringsEn() {
    }
    public StringsEn(StringsRequests stringsRequests){
        copyProperties(stringsRequests);
        setNewImages(null);

    }

    public void copyProperties(StringsRequests stringsRequests) {
        BeanUtils.copyProperties(stringsRequests, this);
    }

}
