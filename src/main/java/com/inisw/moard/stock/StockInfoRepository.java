package com.inisw.moard.stock;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StockInfoRepository extends JpaRepository<StockInfo, Long> {
	Optional<StockInfo> findByName(String query);
}
