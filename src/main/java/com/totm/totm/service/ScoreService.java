package com.totm.totm.service;

import com.totm.totm.repository.BaseballScoreRepository;
import com.totm.totm.repository.BasketballScoreRepository;
import com.totm.totm.repository.FootballScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.totm.totm.dto.ScoreDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScoreService {

    private final FootballScoreRepository footballScoreRepository;
    private final BaseballScoreRepository baseballScoreRepository;
    private final BasketballScoreRepository basketballScoreRepository;

    public Page<FootballRankingResponseDto> getFootballRanking(int year, Pageable pageable) {
        return footballScoreRepository.findFootballRankingByYearAndScore(year, pageable)
                .map(FootballRankingResponseDto::new);
    }

    public Page<BaseballRankingResponseDto> getBaseballRanking(int year, Pageable pageable) {
        return baseballScoreRepository.findBaseballRankingByYearAndScore(year, pageable)
                .map(BaseballRankingResponseDto::new);
    }

    public Page<BasketballRankingResponseDto> getBasketballRanking(int year, Pageable pageable) {
        return basketballScoreRepository.findBasketballRankingByYearAndScore(year, pageable)
                .map(BasketballRankingResponseDto::new);
    }

}
