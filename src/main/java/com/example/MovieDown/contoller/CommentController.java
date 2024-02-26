package com.example.MovieDown.contoller;

import com.example.MovieDown.model.Comment;
import com.example.MovieDown.model.Movie;
import com.example.MovieDown.model.User;
import com.example.MovieDown.service.CommentService;
import com.example.MovieDown.service.MovieService;
import com.example.MovieDown.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private MovieService movieService;

    @PostMapping("/comment")
    public String writeComment(@RequestParam("content") String content,
                               @RequestParam("movieId") Long movieId,
                               @AuthenticationPrincipal UserDetails userDetails){
        User user = userService.findByUsername(userDetails.getUsername());
        Movie movie = movieService.findById(movieId);
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setDate(LocalDateTime.now());
        comment.setUser(user);
        comment.setMovie(movie);
        comment.setUserName(userDetails.getUsername());
        commentService.addComment(comment);
        return "redirect:/movie/movies/" + movieId;
    }
    @PostMapping("/comment/update/{id}")
    public String updateComment(@PathVariable("id") Long id,
                                @RequestBody Map<String, String> request) {
        String content = request.get("content");
        Comment comment = commentService.getCommentById(id);
        comment.setContent(content);
        commentService.updateComment(comment);
        return "/MainHome"; // 댓글 수정 후 댓글 리스트 페이지로 리다이렉트
    }
    @DeleteMapping("/comment/{id}")
    public String deleteComment(@PathVariable("id") Long id) {
        commentService.deleteComment(id);
        return "/MainHome"; // 댓글 삭제 후 댓글 리스트 페이지로 리다이렉트
    }
}
