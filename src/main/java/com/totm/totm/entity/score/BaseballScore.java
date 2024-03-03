package com.totm.totm.entity.score;

import com.totm.totm.entity.BaseEntity;
import com.totm.totm.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BaseballScore extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int score;

    @Version
    @Column
    private Long version;

    public BaseballScore(Member member, int year, int score) {
        this.member = member;
        this.year = year;
        this.score = score;
        member.getBaseballScores().add(this);
    }
}
