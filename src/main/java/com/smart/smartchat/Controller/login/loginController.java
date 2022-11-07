package com.smart.smartchat.Controller.login;

import com.smart.smartchat.Bean.user;
import com.smart.smartchat.Service.login.loginService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class loginController
{
    @Autowired
    loginService loginService;


    @GetMapping(value = {"/", "login"})
    public String login01()
    {
        return "login/login";
    }

    @PostMapping("login")
    public String login02(user user, HttpSession session)
    {
        if (loginService.userIsNull(user)){
            session.setAttribute("status", "账户密码不能为空！");
        }
        else if (loginService.isLoginSuccess(user))
        {
            session.setAttribute("username",user.getUsername());
            session.setAttribute("userId",user.getId());
            return "redirect:friendView";
        } else
        {
            String tips = loginService.loginFail(user);
            session.setAttribute("status", tips);
        }
            return "redirect:login";
    }
    @GetMapping("/test1")
    public String close(){
       return "test1";
    }
}

