package com.inisw.moard.content;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.inisw.moard.searchquery.SearchQuery;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contents")
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "text")
    private String title;
    @Column(columnDefinition = "text")
    private String description;
    @Column(columnDefinition = "text")
    private String url;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "text")
    private ContentType type;
    @Column(name = "image_url", columnDefinition = "text")
    private String imageUrl;
    @Column(name = "embedding", columnDefinition = "text")
    private String embedding;
    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @ManyToOne
    @JoinColumn(name = "search_query_id")
    @JsonBackReference
    private SearchQuery searchQuery;

    public void setSearchQuery(SearchQuery searchQuery) {
        this.searchQuery = searchQuery;
        if (searchQuery != null && !searchQuery.getContentList().contains(this)) {
            searchQuery.getContentList().add(this);
        }
    }
}
