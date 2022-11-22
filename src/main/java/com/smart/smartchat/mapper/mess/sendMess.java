package com.smart.smartchat.mapper.mess;


import com.smart.smartchat.Bean.text;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface sendMess
{
    @Insert("INSERT INTO message(nameA,nameB,message,time,messType) values (#{nameA},#{nameB},#{message},NOW(),#{messType})")
    public Integer insertMess(String nameA,String nameB,String message,Integer messType);
//    @Select("select * from message where nameA=#{nameA} and nameB=#{nameB} and status=#{status} and messType=#{messType}")
    public List<text>selectMess(String nameA,String nameB, Integer status,Integer messType);
    @Select("select count(*) from friendInfo where id=#{id} and fid=#{fid}")
    public Integer messNumber(Integer id,Integer fid);
    @Update("update message set status=#{status},message=#{message} where nameA=#{nameA} and nameB=#{nameB} and messType=#{messType} and message=#{oldmess}")
    public Integer updateMessage(Integer status,String message,String nameA,String nameB,Integer messType,String oldmess);
    @Update("update message set status=1 where nameA=#{nameA} and nameB=#{nameB} and messType=#{messType} or nameA=#{nameB} and nameB=#{nameA} and messType=#{messType}")
    public Integer updateMessageStatus(String nameA,String nameB,Integer messType);
    @Select("SELECT * FROM message where (nameA=#{nameA} and nameB=#{nameB} and messType=#{messType}) or (nameA=#{nameB} and nameB=#{nameA} and messType=#{messType}) ORDER BY time ")
    public List<text> selectAllMess(String nameA,String nameB,Integer messType);
    @Select("select * from message where nameB=#{username} and messType=0")
    public List<text> selectSysMess(String username);
    //查找所有未读的消息
    @Select("select * from message where nameB=#{username} and status=#{status} and messType=#{messType} or nameA=#{username} and status=#{status} and messType=#{messType} order by time desc")
    public List<text> unreadMessFor(String username,Integer status,Integer messType);
}