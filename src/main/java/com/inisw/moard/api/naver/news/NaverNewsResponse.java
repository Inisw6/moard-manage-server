package com.inisw.moard.api.naver.news;

import lombok.Getter;

import java.util.List;

@Getter
public class NaverNewsResponse {
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<NaverNewsItem> items;
}