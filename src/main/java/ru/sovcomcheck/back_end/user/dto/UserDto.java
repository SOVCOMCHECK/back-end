package ru.sovcomcheck.back_end.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Builder
public class UserDto {
    private String id;
    private String email;
    private String firstname;
    private String lastname;
    private String avatar;
}