package com.smart.smartchat;

import com.smart.smartchat.Bean.information;
import com.smart.smartchat.Bean.text;
import com.smart.smartchat.Service.friendView.friendView;
import com.smart.smartchat.Service.mess.message;
import com.smart.smartchat.mapper.friendView.checkFriend;
import com.smart.smartchat.mapper.login.check_user;
import com.smart.smartchat.mapper.mess.sendMess;
import com.smart.smartchat.mapper.register.addUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class SmartChatApplicationTests
{
    @Autowired
    check_user check_user;
    @Autowired
    addUser addUser;
    @Autowired
    checkFriend checkFriend;
    @Autowired
    friendView friendView;
    @Autowired
    sendMess sendMess;
    @Autowired
    message message;
    @Value("${login.maxNumber}")
    private int a;
    @Value("${login.maxLockTime}")
    private long b;
    @Value("${register.minLength}")
    private Integer minLength;
    @Value("${register.maxLength}")
    private Integer maxLength;

    @Test
    void contextLoads()
    {
//        check_user.insertTime(new user("admin","123"));
//        System.out.println(a);
//        System.out.println(check_user.selectUser(new user("admin", "1234567")));
    }
    @Test
    void testTime(){
//        System.out.println(check_user.selectTime(new user("1", "123")));
//        System.out.println(check_user.selectTime(new user("66", "123")));
//        Timestamp y = check_user.selectTime(new user("1", "132"));
//        Timestamp x = new Timestamp(System.currentTimeMillis());
//        System.out.println("x:"+x);
//        System.out.println("y:"+y);
//        System.out.println(x.getTime());
//        System.out.println(y.getTime());
//        System.out.println(new Date(y.getTime()+1000));
//        System.out.println(b);
//        check_user.selectNumber(new user("1", "132"));
    }
    @Test
    void testLength(){
        System.out.println(maxLength);

    }
    @Test
    void testRepeat(){
        String a="1571806964";
        String mess="{\"OnLine\":"+a+"}";
        System.out.println(mess);
//        System.out.println(addUser.checkRepeat(a));
    }
    @Test
    void testUSER(){
        System.out.println(checkFriend.check(1, 0));
    }
//    @Test
//    void testID(){
//        log.info(friendView.checkFriend01("admin"));
//    }
//    @Test
//    void testFri(){
//        System.out.println(friendView.checkFri("admin"));
////    }
//    @Test
//    void testMess(){
//        System.out.println(sendMess.addFriends(1, 2));
//    }
    @Test
    void testCount()
    {
//        System.out.println(sendMess.addIsRepeat(1,2));
        String a="qqq";
        String b="qqq";
        String c=new String("qqq");
        System.out.println(a==b);
        System.out.println(a==c);
        text text = new text();
    }
    @Test
    void testTIME(){
        List<text> texts = sendMess.selectMess("admin", "aaasss12", 0, 0);
        System.out.println(texts.get(0).getTime());
    }
    @Test
    void aad(){
//        System.out.println(friendView.search("aaasss12"));
        System.out.println(sendMess.selectMess("admin", "aaasss12", null, 0));
    }

}
