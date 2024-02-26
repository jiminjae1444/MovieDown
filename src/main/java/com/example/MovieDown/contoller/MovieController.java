package com.example.MovieDown.contoller;

import com.example.MovieDown.model.Comment;
import com.example.MovieDown.model.Movie;
import com.example.MovieDown.repository.MovieRepository;
import com.example.MovieDown.service.CommentService;
import com.example.MovieDown.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/movie")
public class MovieController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/form")
    public String Form() {
        return "movie/MovieRegisterForm";
    }

    @PostMapping("/form")
    public String Form(@RequestParam("poster") MultipartFile poster, @RequestParam String title, @RequestParam String director,
                       @RequestParam String category, @RequestParam int time, @RequestParam String age,
                       @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                       @RequestParam String cast, @RequestParam int price, @RequestParam String url, @RequestParam String synobsis) throws Exception {
        Movie movie = new Movie();
        try {
            // 포스터를 바이트 배열로 변환하여 저장
            byte[] posterBytes = poster.getBytes();
            movie.setPoster(posterBytes);
        } catch (IOException e) {
            e.printStackTrace();
            // 예외 처리
        }
        movie.setTitle(title);
        movie.setDirector(director);
        movie.setCategory(category);
        movie.setTime(time);
        movie.setAge(age);
        movie.setDate(date);
        movie.setCast(cast);
        movie.setPrice(price);
        movie.setUrl(url);
        movie.setSynobsis(synobsis);
        movieService.save(movie);
        return "redirect:/MainHome";
    }

    @GetMapping("/movies/{id}")
    public String view(@PathVariable Long id, Model model) {

        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie != null) {
            model.addAttribute("movie", movie);

            // 포스터 바이트 배열을 Base64로 인코딩하고 모델에 추가
            String base64EncodedPoster = Base64.getEncoder().encodeToString(movie.getPoster());
            model.addAttribute("base64EncodedPoster", base64EncodedPoster);
        }
        List<Comment> commentList = commentService.getCommentsByMovieId(id);
        model.addAttribute("comments", commentList);
        return "movie/MovieDownView";
    }

    @GetMapping("/movieList")
    public String list(@RequestParam(name = "age", required = false) String age,
                       @RequestParam(name = "category", required = false) String category,
                       @RequestParam(required = false, defaultValue = "") String searchText,
                       Model model, @PageableDefault(size = 8) Pageable pageable) {
        Page<Movie> moviePage;
        if (!searchText.isEmpty()) {
            // 검색 기능
            moviePage = movieRepository.findByTitleContaining(searchText, pageable);
        } else if (category != null && !category.isEmpty()) {
            // 카테고리 기능
            moviePage = movieService.getMoviesByCategory(category, pageable);
        } else if (age != null && !age.isEmpty()) {
            // 연령대 기능
            moviePage = movieService.getMoviesByAge(age, pageable);
        } else {
            // 전체 목록 기능
            moviePage = movieService.getAllMovies(pageable);
        }

        int block = 5;
        int currentBlock = (moviePage.getPageable().getPageNumber() / block) * block;
        int startPage = 1 + currentBlock;
        int endPage = Math.min(moviePage.getTotalPages(), currentBlock + block);

//         각 Movie에 대한 포스터를 Base64로 인코딩하여 리스트에 추가
        List<String> base64EncodedPosters = moviePage.getContent().stream()
                .map(movie -> Base64.getEncoder().encodeToString(movie.getPoster()))
                .collect(Collectors.toList());

        model.addAttribute("movies", moviePage.getContent());
        model.addAttribute("moviePage", moviePage);
        model.addAttribute("base64EncodedPosters", base64EncodedPosters);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("selectedCategory", category); // 선택된 카테고리를 모델에 추가
        model.addAttribute("selectedAge", age); // 선택된 관람등급을 모델에 추가
        model.addAttribute("searchText", searchText);
        return "movie/list";
    }

    @PreAuthorize("hasRole('ADMIN') or #movie.user.username == authentication.name")
    @DeleteMapping("delete/{id}")
    @Transactional
    public String deleteMovie(@PathVariable Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow();
        movieService.delete(id);
        return "/MainHome";
    }
//    @GetMapping("/search")
//    public String searchMovie(Model model,@RequestParam(required = false,defaultValue = "") String searchText,@PageableDefault(size = 8)Pageable pageable){
//        Page<Movie> searchMovie = movieRepository.findByTitleContaining(searchText, pageable);
//        model.addAttribute("movies", searchMovie.getContent());
//        model.addAttribute("moviePage", searchMovie);
//        model.addAttribute("base64EncodedPosters", searchMovie.getContent().stream()
//                .map(movie -> Base64.getEncoder().encodeToString(movie.getPoster()))
//                .collect(Collectors.toList()));
//        model.addAttribute("searchText", searchText); // 검색어를 모델에 추가
//        return "movie/list";
//    }
}
