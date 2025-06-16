package com.inisw.moard.user.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserLogRepository extends JpaRepository<UserLog, Long> {
	@Query(
		value = "SELECT COUNT(*) FROM user_logs u WHERE DATE(u.timestamp) = CURDATE()",
		nativeQuery = true
	)
	Long countTodayNative();
}
