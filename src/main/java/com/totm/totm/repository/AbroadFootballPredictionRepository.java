package com.totm.totm.repository;

import com.totm.totm.entity.AbroadFootballGame;
import com.totm.totm.entity.AbroadFootballPrediction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AbroadFootballPredictionRepository extends MongoRepository<AbroadFootballPrediction, String> {

    Optional<AbroadFootballPrediction> findByAbroadFootballGameAndMemberId(AbroadFootballGame abroadFootballGame, Long memberId);

    List<AbroadFootballPrediction> findByAbroadFootballGame(AbroadFootballGame abroadFootballGame);
}
