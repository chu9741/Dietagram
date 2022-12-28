package com.example.Dietagram.repository;

import com.example.Dietagram.domain.FeedComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {
}
