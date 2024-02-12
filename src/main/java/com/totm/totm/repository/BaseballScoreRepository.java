package com.totm.totm.repository;

import com.totm.totm.entity.BaseballScore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BaseballScoreRepository extends JpaRepository<BaseballScore, Long> {

    @Query("select bs from BaseballScore bs join fetch bs.member m" +
            " where bs.year = :year and bs.score > 0 order by bs.score desc")
    Page<BaseballScore> findBaseballRankingByYearAndScore(int year, Pageable pageable);

    @Query("select bs from BaseballScore bs where bs.member.id = :id and bs.year = :year")
    Optional<BaseballScore> findBaseballScoreByYearAndMemberId(Long id, int year);

    @Query("select count(bs) from BaseballScore bs where bs.year = :year and bs.score > :score")
    int findRank(int year, int score);

    @Query("select count(bs) from BaseballScore bs where bs.year = :year and bs.score > 0")
    int findParticipants(int year);
}
