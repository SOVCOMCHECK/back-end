package ru.sovcomcheck.back_end.user.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "user")
@Builder
@Getter
@Setter
public class User {
    @Id
    private String id;

    @Field(name = "email")
    private String email;

    @Field(name = "password")
    private String password;

    @Field(name = "firstname")
    private String firstname;

    @Field(name = "lastname")
    private String lastname;

    @Field(name = "avatar")
    private String avatar;
}
