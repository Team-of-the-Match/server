package com.totm.totm.repository;

import com.totm.totm.entity.BaseballScore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BaseballScoreRepository extends JpaRepository<BaseballScore, Long> {

    @Query("select bs from BaseballScore bs join fetch bs.member m" +
            " where bs.year = :year and bs.score > 0 order by bs.score desc")
    Page<BaseballScore> findBaseballRankingByYearAndScore(int year, Pageable pageable);
}
