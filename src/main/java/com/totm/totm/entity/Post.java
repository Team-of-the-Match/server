package com.totm.totm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    @Column(nullable = false)
    private int likeNum;

    @Column(nullable = false)
    private int commentNum;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    @JsonIgnore
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    public Post(String title, String content, PostStatus postStatus, int likeNum, int commentNum) {
        this.title = title;
        this.content = content;
        this.postStatus = postStatus;
        this.likeNum = likeNum;
        this.commentNum = commentNum;
    }

    public void setMember(Member member) {
        this.member = member;
        member.getPosts().add(this);
    }

    public void changeStatus(PostStatus postStatus) {
        this.postStatus = postStatus;
    }

    public void modifyPost(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public int increaseLike() {
        return ++this.likeNum;
    }

    public int decreaseLike() {
        return --this.likeNum;
    }

    public void increaseComment() {
        this.commentNum++;
    }

    public void decreaseComment() {
        this.commentNum--;
    }
}
