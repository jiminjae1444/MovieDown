package com.example.MovieDown.repository;

import com.example.MovieDown.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByUsername(String username);

    @Query("SELECT u FROM User u JOIN FETCH u.userInfo WHERE u.username = :username")
    User findByUsername(String username);
}
