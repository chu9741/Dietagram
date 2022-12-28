package com.example.Dietagram.repository;

import com.example.Dietagram.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndProvider(String email, String provider);
    User findByNickname(String nickname);
    User findByAttributeId(String attributeId);
    List<User> findByNicknameContainingIgnoreCase(String keyword);
}
