package com.smart.smartchat.Service.mess;

public interface message
{
    public Integer addFriends(String nameA, String nameB);

    public String selectMess(String nameA, String nameB, Integer status, Integer messType);

    public boolean IsRepeat(String nameA, String nameB, String message, Integer status, Integer messType);

    public String chat(String nameA, String nameB, String message);
}
