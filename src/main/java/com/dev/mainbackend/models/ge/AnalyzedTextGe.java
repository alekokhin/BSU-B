package com.dev.mainbackend.models.ge;

import com.dev.mainbackend.request.AnalyzedTextRequests;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Data
@Document(collection = "analyzed-text-ge")
public class AnalyzedTextGe {
    @Id
    private String id;
    private String title;
    private String description;
    private String connection;

    public AnalyzedTextGe() {

    }

    public AnalyzedTextGe(String title,String description, String connection) {
        this.title = title;
        this.description = description;
        this.connection = connection;
    }

    public AnalyzedTextGe(AnalyzedTextRequests analyzedTextRequests) {
        copyProperties(analyzedTextRequests);

    }

    public void copyProperties(AnalyzedTextRequests analyzedTextRequests) {
        BeanUtils.copyProperties(analyzedTextRequests, this);
    }
}
