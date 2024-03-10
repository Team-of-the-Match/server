package com.totm.totm.repository.score;

import com.totm.totm.entity.score.FootballScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FootballScoreRepository extends JpaRepository<FootballScore, Long> {

    @Query("select fs from FootballScore fs join fetch fs.member where fs.year = :year and fs.member.id = :memberId")
    Optional<FootballScore> findByYearAndMemberId(int year, Long memberId);

}
