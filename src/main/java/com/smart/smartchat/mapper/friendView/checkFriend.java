package com.smart.smartchat.mapper.friendView;

import com.smart.smartchat.Bean.user;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface checkFriend
{
    @Select("SELECT f.fid as id,u.username,u.status FROM friendinfo f JOIN userinfo u on u.id=f.fid where f.relation=#{relation} and f.id=#{id}")
    public List<user> check(Integer id, Integer relation);
    @Select("SELECT id from userinfo where username=#{username}")
    public Integer selectId(String username);
    @Select("select u.username FROM friendinfo f JOIN userinfo u on u.id=f.fid where f.id=(SELECT id from userinfo where username=#{username}) and u.status=1")
    public List<String> selectName(String username);
    @Select("update userinfo set status=0 where username=#{username}")
    public Integer exit(String username);
    @Select("SELECT username,status from userinfo where username=#{username}")
    public user search(String username);
    @Insert("insert into friendinfo values(#{id},#{fid},#{relation})")
    public Integer addRelation(Integer id,Integer fid,Integer relation);

}
