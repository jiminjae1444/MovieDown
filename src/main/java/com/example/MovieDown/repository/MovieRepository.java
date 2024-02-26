package com.example.MovieDown.repository;

import com.example.MovieDown.model.Movie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie,Long> {
    Page<Movie> findByCategory(String category, Pageable pageable);

    Page<Movie> getMovieByAge(String age, Pageable pageable);
    Page<Movie> findByTitleContaining(String searchText, Pageable pageable);
}
