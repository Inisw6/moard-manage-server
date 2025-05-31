package com.inisw.moard.user.log.stock;

import java.util.UUID;

public record StockLogRequestDto(
	String stockName,
	UUID userId
) {
}
