package com.inisw.moard.user.log.stock;

import org.springframework.stereotype.Service;

import com.inisw.moard.user.User;
import com.inisw.moard.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockLogService {
	private final StockLogRepository stockLogRepository;
	private final UserRepository userRepository;

	public void createStockLog(StockLogRequestDto requestDto) {
		User user = userRepository.findByUuid(requestDto.userId()).orElse(null);
		StockLog stockLog = StockLog.builder()
			.stockName(requestDto.stockName())
			.user(user)
			.build();

		stockLogRepository.save(stockLog);
	}

}
