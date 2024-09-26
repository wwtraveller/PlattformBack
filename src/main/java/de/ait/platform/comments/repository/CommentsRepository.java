package de.ait.platform.comments.repository;

import de.ait.platform.comments.entity.Comment;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;



public interface CommentsRepository extends JpaRepository<Comment, Long> {
}
