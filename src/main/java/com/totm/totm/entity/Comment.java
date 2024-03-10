package com.totm.totm.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Lob
    private String comment;

    @Enumerated(EnumType.STRING)
    private CommentStatus commentStatus;

    public Comment(String comment, CommentStatus commentStatus) {
        this.comment = comment;
        this.commentStatus = commentStatus;
    }

    public void setMemberAndPost(Member member, Post post) {
        this.member = member;
        member.getComments().add(this);
        this.post = post;
        post.getComments().add(this);
    }

    public void changeStatus(CommentStatus commentStatus) {
        this.commentStatus = commentStatus;
    }
}
