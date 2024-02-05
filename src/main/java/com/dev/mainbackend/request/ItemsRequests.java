package com.dev.mainbackend.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ItemsRequests {
    private String title;
    private String description;
    private List<String> images;
    private List<MultipartFile> newImages;
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
}
