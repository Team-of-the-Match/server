package com.totm.totm.repository;

import com.totm.totm.entity.FootballScore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FootballScoreRepository extends JpaRepository<FootballScore, Long> {

    @Query("select fs from FootballScore fs join fetch fs.member m" +
            " where fs.year = :year and fs.score > 0 order by fs.score desc")
    Page<FootballScore> findFootballRankingByYearAndScore(int year, Pageable pageable);
}
