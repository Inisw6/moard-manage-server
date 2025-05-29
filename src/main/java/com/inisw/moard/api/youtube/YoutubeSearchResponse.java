package com.inisw.moard.api.youtube;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class YoutubeSearchResponse {
    private List<Item> items;

    @Getter
    public static class Item {
        private Id id;
        private Snippet snippet;
    }

    @Getter
    public static class Id {
        private String videoId;
    }

    @Getter
    public static class Snippet {
        private String publishedAt;
        private String title;
        private String description;
        private Thumbnails thumbnails;

        @Getter
        public static class Thumbnails {
            private Thumbnail defaultThumbnail;
            private Thumbnail medium;
            private Thumbnail high;

            @JsonProperty("default")
            public void setDefaultThumbnail(Thumbnail defaultThumbnail) {
                this.defaultThumbnail = defaultThumbnail;
            }
        }

        @Getter
        public static class Thumbnail {
            private String url;
            private int width;
            private int height;
        }
    }
}