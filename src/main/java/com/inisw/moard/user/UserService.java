package com.inisw.moard.user;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	public User createUser(UUID uuid) {
		User user = User.builder()
			.uuid(uuid)
			.build();
		return userRepository.save(user);
	}

	public User findUserById(UUID uuid) {
		return userRepository.findByUuid(uuid).orElse(null);
	}

	public List<User> findAllUserList() {
		return userRepository.findAll();
	}
}
