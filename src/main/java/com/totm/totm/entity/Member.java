package com.totm.totm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String nickname;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Enumerated(value = EnumType.STRING)
    private Authority authority;

    private LocalDate lastConnectedDate;

    private LocalDateTime stopDeadline;

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    @JsonIgnore
    List<FootballScore> footballScores = new ArrayList<>();

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    @JsonIgnore
    List<BaseballScore> baseballScores = new ArrayList<>();

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    @JsonIgnore
    List<BasketballScore> basketballScores = new ArrayList<>();

    public Member(String username, String password, String nickname, String name, String phoneNumber, Authority authority) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.authority = authority;
    }

    public void changeStopDeadline(LocalDateTime stopDeadline) {
        this.stopDeadline = stopDeadline;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void changePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
