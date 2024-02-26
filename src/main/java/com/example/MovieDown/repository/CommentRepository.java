package com.example.MovieDown.repository;

import com.example.MovieDown.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByMovieId(Long movieId);
}
