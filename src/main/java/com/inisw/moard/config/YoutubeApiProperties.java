package com.inisw.moard.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "youtube.api")
@Getter
@Setter
public class YoutubeApiProperties {
    private List<String> keys;
} 