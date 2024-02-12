package com.totm.totm.service;

import com.totm.totm.entity.BaseballScore;
import com.totm.totm.entity.BasketballScore;
import com.totm.totm.entity.FootballScore;
import com.totm.totm.exception.ScoreNotExistException;
import com.totm.totm.repository.BaseballScoreRepository;
import com.totm.totm.repository.BasketballScoreRepository;
import com.totm.totm.repository.FootballScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public MyFootballRankingResponseDto getMyFootballRanking(Long id, int year) {
        Optional<FootballScore> findFootballScore = footballScoreRepository.findFootballScoreByYearAndMemberId(id, year);
        if(findFootballScore.isEmpty()) throw new ScoreNotExistException("점수를 찾을 수 없습니다.");

        int rank = footballScoreRepository.findRank(year, findFootballScore.get().getScore()) + 1;
        int participants = footballScoreRepository.findParticipants(year);
        PageRequest pr = PageRequest.of(0, 10);

        return new MyFootballRankingResponseDto(
                findFootballScore.get().getScore(),
                participants,
                rank,
                footballScoreRepository.findFootballRankingByYearAndScore(year, pr));
    }

    public MyBaseballRankingResponseDto getMyBaseballRanking(Long id, int year) {
        Optional<BaseballScore> findBaseballScore = baseballScoreRepository.findBaseballScoreByYearAndMemberId(id, year);
        if(findBaseballScore.isEmpty()) throw new ScoreNotExistException("점수를 찾을 수 없습니다.");

        int rank = baseballScoreRepository.findRank(year, findBaseballScore.get().getScore()) + 1;
        int participants = baseballScoreRepository.findParticipants(year);
        PageRequest pr = PageRequest.of(0, 10);

        return new MyBaseballRankingResponseDto(
                findBaseballScore.get().getScore(),
                participants,
                rank,
                baseballScoreRepository.findBaseballRankingByYearAndScore(year, pr));
    }

    public MyBasketballRankingResponseDto getMyBasketballRanking(Long id, int year) {
        Optional<BasketballScore> findBasketballScore = basketballScoreRepository.findBasketballScoreByYearAndMemberId(id, year);
        if(findBasketballScore.isEmpty()) throw new ScoreNotExistException("점수를 찾을 수 없습니다.");

        int rank = basketballScoreRepository.findRank(year, findBasketballScore.get().getScore()) + 1;
        int participants = basketballScoreRepository.findParticipants(year);
        PageRequest pr = PageRequest.of(0, 10);

        return new MyBasketballRankingResponseDto(
                findBasketballScore.get().getScore(),
                participants,
                rank,
                basketballScoreRepository.findBasketballRankingByYearAndScore(year, pr));
    }
}
