package com.dev.mainbackend.models.ge;

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
@Document(collection = "strings-ge")
public class StringsGe {
    @Id
    private String id;
    private String title;
    private String description;
    private String connection;
    private List<String> images;
    private List<MultipartFile> newImages = new ArrayList<>();
    public StringsGe(String title, String description, String connection, List<String> images,List<MultipartFile> newImages) {
        this.title = title;
        this.description = description;
        this.connection = connection;
        this.images = images;
        this.newImages = newImages;

    }

    public StringsGe() {
    }
    public StringsGe(StringsRequests stringsRequests){
        copyProperties(stringsRequests);
        setNewImages(null);

    }

    public void copyProperties(StringsRequests stringsRequests) {
        BeanUtils.copyProperties(stringsRequests, this);
    }

}
