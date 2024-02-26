package com.example.MovieDown.contoller;

import com.example.MovieDown.model.Movie;
import com.example.MovieDown.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController {
    @Autowired
    private MovieService movieService;
    @GetMapping("/MainHome")
    public String index(Model model , @RequestParam(name = "page",defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "6")int size){

        // 페이징 정보를 포함한 데이터 가져오기
//        Page<Movie> moviePage = movieService.getAllMovies(PageRequest.of(page,size));

        List<Movie> selectedMovies = movieService.getRandomMovies(6);
        List<String> base64EncodedPosters = selectedMovies.stream()
                .map(movie -> Base64.getEncoder().encodeToString(movie.getPoster()))
                .collect(Collectors.toList());
        // 각 Movie에 대한 포스터를 Base64로 인코딩하여 리스트에 추가
//        List<String> base64EncodedPosters = new ArrayList<>();
//        for (Movie movie : moviePage.getContent()) {
//            String base64EncodedPoster = Base64.getEncoder().encodeToString(movie.getPoster());
//            base64EncodedPosters.add(base64EncodedPoster);
//        }


        // 템플릿에 페이징된 데이터 및 페이지 정보 전달
//        model.addAttribute("movies", moviePage.getContent());
        model.addAttribute("movies", selectedMovies);
        model.addAttribute("base64EncodedPosters", base64EncodedPosters);
//        model.addAttribute("currentPage", page);
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);
//        model.addAttribute("totalPages", moviePage.getTotalPages());
        return "MainHome";

    }
}
