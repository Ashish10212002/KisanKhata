package com.ashish.farm.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String name; // used for signup
    private String email;
    private String password;
}
