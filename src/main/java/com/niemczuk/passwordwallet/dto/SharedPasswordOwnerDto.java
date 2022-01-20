package com.niemczuk.passwordwallet.dto;

import com.niemczuk.passwordwallet.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SharedPasswordOwnerDto {

    private UUID id;

    private String login;

    private String password;

    private String webAddress;

    private String description;

    private User sharedTo;
}
