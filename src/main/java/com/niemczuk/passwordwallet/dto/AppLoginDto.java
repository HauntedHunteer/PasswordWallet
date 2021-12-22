package com.niemczuk.passwordwallet.dto;

import com.niemczuk.passwordwallet.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppLoginDto {

    private User user;

    private LocalDateTime loginTime;

    private String loginResult;

    private String ipAddress;
}
