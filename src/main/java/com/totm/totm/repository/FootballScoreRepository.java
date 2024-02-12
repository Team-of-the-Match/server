package com.totm.totm.repository;

import com.totm.totm.entity.FootballScore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FootballScoreRepository extends JpaRepository<FootballScore, Long> {

    @Query("select fs from FootballScore fs join fetch fs.member m" +
            " where fs.year = :year and fs.score > 0 order by fs.score desc")
    Page<FootballScore> findFootballRankingByYearAndScore(int year, Pageable pageable);

    @Query("select fs from FootballScore fs where fs.member.id = :id and fs.year = :year")
    Optional<FootballScore> findFootballScoreByYearAndMemberId(Long id, int year);

    @Query("select count(fs) from FootballScore fs where fs.year = :year and fs.score > :score")
    int findRank(int year, int score);

    @Query("select count(fs) from FootballScore fs where fs.year = :year and fs.score > 0")
    int findParticipants(int year);
}
