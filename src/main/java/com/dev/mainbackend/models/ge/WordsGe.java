package com.dev.mainbackend.models.ge;

import com.dev.mainbackend.request.WordRequest;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Data
@Document(collection = "words-ge")
public class WordsGe {
    @Id
    private String id;
    private String word;
    private String correctForm;
    private String intonation;
    private String thematicGroup;
    private String partOfSpeech;
    private String dictionary;

    public WordsGe() {
    }

    public WordsGe(String id, String word, String correctForm, String intonation, String thematicGroup, String partOfSpeech,String dictionary) {
        this.id = id;
        this.word = word;
        this.correctForm = correctForm;
        this.intonation = intonation;
        this.thematicGroup = thematicGroup;
        this.partOfSpeech = partOfSpeech;
        this.dictionary = dictionary;
    }

    public WordsGe(WordRequest wordRequests) {
        copyProperties(wordRequests);

    }

    public void copyProperties(WordRequest wordRequest) {
        BeanUtils.copyProperties(wordRequest, this);
    }
}
