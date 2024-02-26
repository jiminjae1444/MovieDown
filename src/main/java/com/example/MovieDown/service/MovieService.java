package com.example.MovieDown.service;

import com.example.MovieDown.model.Movie;
import com.example.MovieDown.model.User;
import com.example.MovieDown.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void save(Movie movie) throws Exception {
        Movie savedMovie = movieRepository.save(movie);

    }
    public List<Movie> getRandomMovies(int count) {
        List<Movie> allMovies = movieRepository.findAll();
        return getRandomItems(allMovies, count);
    }
    private <T> List<T> getRandomItems(List<T> items, int n) {
        Random random = new Random();
        return items.stream()
                .distinct()
                .limit(n)
                .collect(Collectors.toList());
    }
    public Page<Movie> getAllMovies(Pageable pageable) {
      return movieRepository.findAll(pageable);
    }

    public Page<Movie> getMoviesByCategory(String category, Pageable pageable) {
        return movieRepository.findByCategory(category, pageable);
    }

    public Page<Movie> getMoviesByAge(String age, Pageable pageable){
        return movieRepository.getMovieByAge(age,pageable);
    }

    public void delete(Long id) {
        movieRepository.deleteById(id);
    }

    public Movie findById(Long movieId) {
        return movieRepository.findById(movieId).orElseThrow();
    }
}
