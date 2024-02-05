package com.dev.mainbackend.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WordRequest {
    private String word;
    private String correctForm;
    private String intonation;
    private String thematicGroup;
    private String partOfSpeech;
    private String dictionary;
}
