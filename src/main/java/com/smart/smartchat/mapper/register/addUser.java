package com.smart.smartchat.mapper.register;

import com.smart.smartchat.Bean.user;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface addUser
{
    @Options(useGeneratedKeys=true,keyProperty="id")
    @Insert("insert into userInfo(username,password) values (#{username},#{password})")
    public int addUser(user user);
    @Select("select username from userInfo where username=#{username}")
    public String checkRepeat(String username);
}
