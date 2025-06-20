package com.inisw.moard.user;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@CrossOrigin
public class UserController {
	private final UserService userService;

	@PostMapping
	public User saveUser(@RequestBody UUID uuid) {
		return userService.createUser(uuid);
	}

	@GetMapping("/{uuid}")
	public User getUser(@PathVariable UUID uuid) {
		return userService.findUserById(uuid);
	}

	@GetMapping
	public List<User> getUsers() {
		return userService.findAllUserList();
	}
	
}
