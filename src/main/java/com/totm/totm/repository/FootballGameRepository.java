package com.totm.totm.repository;

import com.totm.totm.entity.FootballGame;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FootballGameRepository extends MongoRepository<FootballGame, String> {

    Optional<FootballGame> findFootballGamesByGameDate(String date);

    List<FootballGame> findFootballGamesByScoreUpdated(boolean scoreUpdated);

    List<FootballGame> findFootballGamesByGameDateContainingAndClosed(String gameDate, boolean closed);

    Optional<FootballGame> findByGameDateAndClosed(String gameDate, boolean closed);

}
