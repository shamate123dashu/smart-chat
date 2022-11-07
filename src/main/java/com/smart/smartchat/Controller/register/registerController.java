package com.smart.smartchat.Controller.register;

import com.smart.smartchat.Bean.user;
import com.smart.smartchat.Service.register.registerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class registerController
{
    @Autowired
    registerService registerService;

    @PostMapping("/register")
    @ResponseBody
    public String register(user user)
    {
        if (registerService.isRepeat(user))
        {
            return "账户名重复";
        } else if (!registerService.isSatisfyDemand(user)||registerService.isNull(user))
        {
            return "Fuck!别动我前端代码！";
        }
        else if (registerService.addUser(user)){
            return "注册成功！";
        }
        else
            return "系统原因！注册失败";

    }

}
