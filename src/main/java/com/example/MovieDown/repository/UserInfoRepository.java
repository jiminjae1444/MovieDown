package com.example.MovieDown.repository;

import com.example.MovieDown.model.User;
import com.example.MovieDown.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {
}
