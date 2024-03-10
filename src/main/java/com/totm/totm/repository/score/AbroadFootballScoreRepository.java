package com.totm.totm.repository.score;

import com.totm.totm.entity.score.AbroadFootballScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AbroadFootballScoreRepository extends JpaRepository<AbroadFootballScore, Long> {

    @Query("select fs from AbroadFootballScore fs join fetch fs.member where fs.year = :year and fs.member.id = :memberId")
    Optional<AbroadFootballScore> findByYearAndMemberId(int year, Long memberId);
}
