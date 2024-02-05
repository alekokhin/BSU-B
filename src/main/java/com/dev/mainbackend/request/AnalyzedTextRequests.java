package com.dev.mainbackend.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalyzedTextRequests {
    private String title;
    private String description;
    private String connection;
}
