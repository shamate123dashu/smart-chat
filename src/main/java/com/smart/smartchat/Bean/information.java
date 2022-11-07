package com.smart.smartchat.Bean;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class information
{
    private String nameA;
    private String nameB;
    private Timestamp time;
    private Integer status;
    private Integer messType;
}
