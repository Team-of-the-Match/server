package com.totm.totm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    List<Comment> comments = new ArrayList<>();

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

    public void confirm() {
        this.confirmed = true;
    }
}
