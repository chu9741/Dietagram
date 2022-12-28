package com.example.Dietagram.repository;

import com.example.Dietagram.domain.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long> {

}
