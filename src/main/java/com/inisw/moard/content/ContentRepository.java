package com.inisw.moard.content;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {
	Optional<Content> findByUrl(String url);
}