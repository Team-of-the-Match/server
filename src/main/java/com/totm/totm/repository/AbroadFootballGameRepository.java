package com.totm.totm.repository;

import com.totm.totm.entity.AbroadFootballGame;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AbroadFootballGameRepository extends MongoRepository<AbroadFootballGame, String> {

    Optional<AbroadFootballGame> findAbroadFootballGamesByGameDate(String date);

    List<AbroadFootballGame> findAbroadFootballGamesByScoreUpdated(boolean scoreUpdated);

    List<AbroadFootballGame> findAbroadFootballGamesByGameDateContainingAndClosed(String gameDate, boolean closed);

    Optional<AbroadFootballGame> findByGameDateAndClosed(String gameDate, boolean closed);

}
