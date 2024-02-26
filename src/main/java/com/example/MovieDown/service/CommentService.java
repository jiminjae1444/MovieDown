package com.example.MovieDown.service;

import com.example.MovieDown.model.Comment;
import com.example.MovieDown.model.Movie;
import com.example.MovieDown.model.User;
import com.example.MovieDown.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;
    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getCommentsByMovieId(Long movieId) {
        return commentRepository.findByMovieId(movieId);
    }
    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    public Comment getCommentById(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        return optionalComment.orElse(null);
    }

    public void updateComment(Comment updatedComment) {
        // 업데이트할 댓글의 ID를 가져옵니다.
        Long commentId = updatedComment.getId();

        // 댓글을 데이터베이스에서 찾습니다.
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        // 데이터베이스에 해당하는 댓글이 있는지 확인합니다.
        if (optionalComment.isPresent()) {
            // 댓글이 존재하면 업데이트합니다.
            Comment existingComment = optionalComment.get();
            // 새로운 내용이 null이 아닌 경우에만 업데이트합니다.
            if (updatedComment.getContent() != null) {
                existingComment.setContent(updatedComment.getContent());
            }
            existingComment.setDate(LocalDateTime.now()); // 업데이트한 날짜 설정 (옵션)

            // 변경된 댓글을 저장합니다.
            commentRepository.save(existingComment);
        } else {
            // 해당 ID에 해당하는 댓글이 없을 경우에 대한 예외 처리
            // 예외 처리를 선택적으로 구현할 수 있습니다.
        }
    }
}
