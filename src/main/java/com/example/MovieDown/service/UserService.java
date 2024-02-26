package com.example.MovieDown.service;

import com.example.MovieDown.model.Role;
import com.example.MovieDown.model.User;
import com.example.MovieDown.model.UserInfo;
import com.example.MovieDown.repository.UserInfoRepository;
import com.example.MovieDown.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    public boolean usernameSame(String username){
        return userRepository.existsByUsername(username);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void save(User user) throws Exception {
        String encoderPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encoderPassword);
        user.setEnabled(true);
        UserInfo userInfo = user.getUserInfo();
        UserInfo savedUserInfo = userInfoRepository.save(userInfo);

        Role role = new Role();
        role.setId(userInfo.isIsadmin() ? 2L : 1L);
        user.getRoles().add(role);

        user.setUserInfo(savedUserInfo);
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
