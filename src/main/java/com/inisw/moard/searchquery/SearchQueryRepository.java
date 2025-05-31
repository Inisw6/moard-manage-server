package com.inisw.moard.searchquery;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchQueryRepository extends JpaRepository<SearchQuery, Long> {
	Optional<SearchQuery> findByQuery(String query);
}
