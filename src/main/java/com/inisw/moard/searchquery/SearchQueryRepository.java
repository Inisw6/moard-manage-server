package com.inisw.moard.searchquery;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SearchQueryRepository extends JpaRepository<SearchQuery, Long> {
    Optional<SearchQuery> findByQuery(String query);
}
