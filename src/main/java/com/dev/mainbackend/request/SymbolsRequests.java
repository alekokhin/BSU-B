package com.dev.mainbackend.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class SymbolsRequests {
    private String title;
    private String description;
    private String connection;
    private List<String> images;
    private List<MultipartFile> newImages;}
