package com.inisw.moard.content;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContentRepository extends JpaRepository<Content, Long> {
	Optional<Content> findByUrl(String url);

	@Query("SELECT c.url FROM Content c WHERE c.url IN :urls")
	List<String> findUrlsByUrlIn(@Param("urls") List<String> urls);
}