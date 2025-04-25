package ru.sovcomcheck.back_end.user.dto;

import lombok.Data;

@Data
public class UserRegisterDto {
    private String id;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
}
