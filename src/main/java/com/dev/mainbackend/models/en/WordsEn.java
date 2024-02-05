package com.dev.mainbackend.models.en;

import com.dev.mainbackend.request.WordRequest;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "words-en")
public class WordsEn {
    private String id;
    private String word;
    private String correctForm;
    private String intonation;
    private String thematicGroup;
    private String partOfSpeech;
    private String dictionary;
    public WordsEn() {
    }

    public WordsEn(String id, String word, String correctForm, String intonation, String thematicGroup, String partOfSpeech) {
        this.id = id;
        this.word = word;
        this.correctForm = correctForm;
        this.intonation = intonation;
        this.thematicGroup = thematicGroup;
        this.partOfSpeech = partOfSpeech;
    }

    public WordsEn(WordRequest wordRequests) {

    }

    public void copyProperties(WordRequest wordRequests) {
        BeanUtils.copyProperties(wordRequests, this);
    }
}
