package com.totm.totm.repository;

import com.totm.totm.entity.FootballGame;
import com.totm.totm.entity.FootballPrediction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FootballPredictionRepository extends MongoRepository<FootballPrediction, String> {

    Optional<FootballPrediction> findByFootballGameAndMemberId(FootballGame footballGame, Long memberId);

    List<FootballPrediction> findByFootballGame(FootballGame footballGame);
}
