package com.dev.mainbackend.models.en;

import com.dev.mainbackend.request.ItemsRequests;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Id;
import java.util.*;

@Data
@Document(collection = "item-en")
public class ItemEn {
    @Id
    private String id;
    private String title;
    private String description;
    private Date date;
    private List<String> images;
    private List<MultipartFile> newImages = new ArrayList<>();
    private String security;
    private String damaged;
    private String color;
    private String structure;
    private String reWriteDate;
    private String reWritePlace;
    private String paperCount;
    private String size;
    private String countOfColumns;
    private String countOfRow;
    private String typeOfPagination;
    private String transcriber;
    private String belonging;
    private String firstAndLast;
    private String will;

    public ItemEn() {

    }

    public ItemEn(String title, String description, Date date,
                  List<String> images,List<MultipartFile> newImages, String security,
                  String damaged, String color,
                  String structure, String reWriteDate,
                  String reWritePlace, String paperCount,
                  String size, String countOfColumns,
                  String countOfRow, String typeOfPagination,
                  String transcriber, String belonging, String firstAndLast,
                  String will) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.images = images;
        this.newImages = newImages;

        this.security = security;
        this.damaged = damaged;
        this.color = color;
        this.structure = structure;
        this.reWriteDate = reWriteDate;
        this.reWritePlace = reWritePlace;
        this.paperCount = paperCount;
        this.size = size;
        this.countOfColumns = countOfColumns;
        this.countOfRow = countOfRow;
        this.typeOfPagination = typeOfPagination;
        this.transcriber = transcriber;
        this.belonging = belonging;
        this.firstAndLast = firstAndLast;
        this.will = will;

    }

    public ItemEn(ItemsRequests itemsRequests) {
        copyProperties(itemsRequests);
        setDate(new Date());
        setNewImages(null);

    }

    public void copyProperties(ItemsRequests itemsRequests) {
        BeanUtils.copyProperties(itemsRequests, this);
    }

}
