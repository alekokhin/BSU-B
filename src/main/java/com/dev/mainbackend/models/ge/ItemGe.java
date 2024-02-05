package com.dev.mainbackend.models.ge;

import com.dev.mainbackend.request.ItemsRequests;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "item-ge")
public class ItemGe {
    @Id
    protected String id;
    protected String title;
    protected String description;
    protected Date date;
    private List<String> images;
    private List<MultipartFile> newImages = new ArrayList<>();
    protected String security;
    protected String damaged;
    protected String color;
    protected String structure;
    protected String reWriteDate;
    protected String reWritePlace;
    protected String paperCount;
    protected String size;
    protected String countOfColumns;
    protected String countOfRow;
    protected String typeOfPagination;
    protected String transcriber;
    protected String belonging;
    protected String firstAndLast;
    protected String will;

    public ItemGe() {

    }

    public ItemGe(String title, String description, Date date,
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

    public ItemGe(ItemsRequests itemsRequests) {
        copyProperties(itemsRequests);
        setDate(new Date());
        setNewImages(null);
    }


    public void copyProperties(ItemsRequests itemsRequests) {
        BeanUtils.copyProperties(itemsRequests, this);
    }

}
