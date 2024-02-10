package com.totm.totm.repository;

import com.totm.totm.entity.BasketballScore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BasketballScoreRepository extends JpaRepository<BasketballScore, Long> {

    @Query("select bs from BasketballScore bs join fetch bs.member m" +
            " where bs.year = :year and bs.score > 0 order by bs.score desc")
    Page<BasketballScore> findBasketballRankingByYearAndScore(int year, Pageable pageable);
}
