package de.ait.platform.user.comments.repository;

import de.ait.platform.user.comments.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comment, Long> {
}
