package com.inisw.moard.user.log.stock;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/stock-log")
@RequiredArgsConstructor
@CrossOrigin
public class StockLogController {
	private final StockLogService stockLogService;

	@PostMapping
	public void save(@RequestBody StockLogRequestDto stockLogRequestDto) {
		stockLogService.createStockLog(stockLogRequestDto);
	}
}
