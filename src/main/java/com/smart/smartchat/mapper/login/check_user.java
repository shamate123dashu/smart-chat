package com.smart.smartchat.mapper.login;


import com.smart.smartchat.Bean.user;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;


@Mapper
public interface check_user
{
    @Select("select * from userInfo where username = #{username} and password = #{password}")
    public user selectUser(user user);
    @Update("update userInfo set status=#{status} where username = #{username}")
    public Integer updateStatus(Integer status ,String username);
    @Insert("insert into lockUser values (#{username},NOW())")
    public int insertTime(user user);
    @Select("select time from lockUser where username=#{username}")
    public Timestamp selectTime(user user);
    @Select("select number from loginNumber where username=#{username}")
    public Integer selectNumber(user user);
    @Insert("insert into loginNumber values (#{username},1)")
    public void insertNumber(user user);
    @Update("update loginNumber set number=number+1 where username = #{username}")
    public void UpdateNumber(user user);
    @Delete("delete from loginNumber where username=#{username}")
    public void deleteNumber(user user);
    @Delete("delete from LockUser where username=#{username}")
    public void deleteLock(user user);
    @Select("SELECT f.fid as id,u.username,u.status FROM friendinfo f JOIN userinfo u on u.id=f.fid where f.id=#{id}")
    public List<user> checkFriend(Integer id);
}
