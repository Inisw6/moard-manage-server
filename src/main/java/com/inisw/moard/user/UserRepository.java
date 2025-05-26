package com.inisw.moard.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUuid(UUID uuid);

	User getReferenceByUuid(UUID uuid);
}
