package com.smart.smartchat.Controller.friendView;

import com.smart.smartchat.Bean.user;
import com.smart.smartchat.Service.friendView.friendView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class friendController
{
    @Autowired
    friendView friendView;
    @GetMapping("/friendView")
    public String friend()
    {
        return "friendView";
    }

    @ResponseBody
    @PostMapping("/friendList")
    public List<user> friendList(
            @RequestParam("relation") Integer relation, HttpSession session)
    {
        Integer id = (Integer)session.getAttribute("userId");
        List<user> userList =friendView.checkFriend(id,relation);
        System.out.println("6666666");
        return userList;
    }
}
