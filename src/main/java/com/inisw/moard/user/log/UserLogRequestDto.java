package com.inisw.moard.user.log;

import java.time.Instant;
import java.util.UUID;

public record UserLogRequestDto(
	UUID userId,
	EventType eventType,
	Long contentId,
	Instant timestamp,
	Integer time,
	Float ratio
) {
}