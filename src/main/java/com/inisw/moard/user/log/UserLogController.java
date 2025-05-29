package com.inisw.moard.user.log;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user-log")
@RequiredArgsConstructor
@CrossOrigin
public class UserLogController {
	private final UserLogService userLogService;

	@PostMapping
	public UserLog createUserLog(@RequestBody UserLogRequestDto userLog) {
		return userLogService.createUserLog(userLog);
	}
}
