package com.totm.totm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.totm.totm.entity.score.*;
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
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String nickname;

    @Column(nullable = false)
    private boolean confirmed;

    private LocalDateTime stopDeadline;

    private LocalDate lastConnectedDate;

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

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    @JsonIgnore
    List<AbroadBasketballScore> abroadBasketballScores = new ArrayList<>();

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    @JsonIgnore
    List<AbroadFootballScore> abroadFootballScores = new ArrayList<>();

    public Member(String email, String password, String nickname, boolean confirmed) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.confirmed = confirmed;
    }

    public void stopMember() {
        this.stopDeadline = LocalDateTime.now().plusDays(7L);
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void setLastConnectedDateToday() {
        this.lastConnectedDate = LocalDate.now();
    }
}
