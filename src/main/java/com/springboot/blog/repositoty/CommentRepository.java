package com.springboot.blog.repositoty;

import com.springboot.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    public List<Comment> findByPostId(long id);
}
