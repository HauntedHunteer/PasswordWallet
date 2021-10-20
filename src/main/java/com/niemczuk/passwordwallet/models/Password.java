package com.niemczuk.passwordwallet.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Password {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String password;

    private String webAddress;

    private String description;

    private String login;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
