package com.inisw.moard.searchquery;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.inisw.moard.content.Content;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "search_queries")
public class SearchQuery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String query;
    @Builder.Default
    private LocalDateTime searchedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "searchQuery", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Content> contentList = new ArrayList<>();
}
