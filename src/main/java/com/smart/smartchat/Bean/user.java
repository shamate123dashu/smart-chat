package com.smart.smartchat.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class user
{
    private Integer id;
    private String username;
    private String password;
    private Integer status;
}
