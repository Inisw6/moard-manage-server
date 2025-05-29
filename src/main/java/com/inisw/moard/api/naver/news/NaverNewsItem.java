package com.inisw.moard.api.naver.news;

import lombok.Getter;

@Getter
public class NaverNewsItem {
    private String title;
    private String originallink;
    private String link;
    private String description;
    private String pubDate;
}