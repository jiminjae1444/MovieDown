package com.example.MovieDown.contoller;

import com.example.MovieDown.model.User;
import com.example.MovieDown.model.UserInfo;
import com.example.MovieDown.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/account")
public class loginController {
    @Autowired
    private UserService userService;
    @GetMapping("/login")
    public String login(){
        return "account/login";
    }
    @GetMapping("/register")
    public String register(){
        return "account/register";
    }
    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String name , @RequestParam String password ,
                           @RequestParam String password2 ,
                           @RequestParam(name = "isAdminHidden", required = false, defaultValue = "0") String isAdminHidden, Model model ) throws Exception {
    if(userService.usernameSame(username)){
        model.addAttribute("usernameError","이미 사용중인 ID입니다.");
        return "account/register";
    }
    if(!password.equals(password2)){
        model.addAttribute("passwordError","비밀번호가 일치하지 않습니다.");
        return "account/register";
    }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        UserInfo userInfo = new UserInfo();
        userInfo.setName(name);
        boolean isAdmin = "1".equals(isAdminHidden);
        userInfo.setIsadmin(isAdmin);
        user.setUserInfo(userInfo); // User 엔티티에 UserInfo 설정
        userService.save(user);
        return "redirect:/";
    }

}
