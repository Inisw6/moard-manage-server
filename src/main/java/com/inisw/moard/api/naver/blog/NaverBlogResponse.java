package com.inisw.moard.api.naver.blog;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NaverBlogResponse {
    private List<NaverBlogItem> items;
}