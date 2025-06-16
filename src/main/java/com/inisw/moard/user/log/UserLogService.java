package com.inisw.moard.user.log;

import org.springframework.stereotype.Service;

import com.inisw.moard.content.Content;
import com.inisw.moard.content.ContentRepository;
import com.inisw.moard.recommendation.Recommendation;
import com.inisw.moard.recommendation.RecommendationRepository;
import com.inisw.moard.user.User;
import com.inisw.moard.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserLogService {
	private final UserLogRepository userLogRepository;
	private final UserRepository userRepository;
	private final ContentRepository contentRepository;
	private final RecommendationRepository recommendationRepository;

	public UserLog createUserLog(UserLogRequestDto userLog) {
		User user = userRepository.getReferenceByUuid(userLog.userId());
		Content content = contentRepository.getReferenceById(userLog.contentId());
		Recommendation recommendation = recommendationRepository.getReferenceById(userLog.recommendationId());
		UserLog userLog1 = UserLog.builder()
			.user(user)
			.content(content)
			.recommendation(recommendation)
			.eventType(userLog.eventType())
			.timestamp(userLog.timestamp())
			.time(userLog.time())
			.ratio(userLog.ratio())
			.build();
		return userLogRepository.save(userLog1);
	}

	public Long getUserLogCount() {
		return userLogRepository.countTodayNative();
	}
}
