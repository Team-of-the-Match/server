package com.totm.totm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column
    private String name;

    @Column(unique = true)
    private String phoneNumber;

    @Enumerated(value = EnumType.STRING)
    private Authority authority;

    private LocalDate lastConnectedDate;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    List<Love> loves = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    List<FootballScore> footballScores = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    List<BaseballScore> baseballScores = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    List<BasketballScore> basketballScores = new ArrayList<>();

    public Member(String username, String password, String nickname, String name, String phoneNumber, MemberStatus memberStatus) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.memberStatus = memberStatus;
    }
}
