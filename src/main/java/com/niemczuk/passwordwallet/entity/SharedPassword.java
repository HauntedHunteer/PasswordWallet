package com.niemczuk.passwordwallet.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SharedPassword {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String sharedPassword;

    @Column(nullable = false)
    private String ownerPassword;

    private String webAddress;

    private String description;

    private String login;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sharedToUser_id", nullable = false)
    private User sharedTo;
}
