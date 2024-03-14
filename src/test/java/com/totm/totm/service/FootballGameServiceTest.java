package com.totm.totm.service;

import com.totm.totm.entity.FootballGame;
import com.totm.totm.entity.Match;
import com.totm.totm.repository.FootballGameRepository;
import com.totm.totm.repository.FootballPredictionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
class FootballGameServiceTest {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    FootballGameRepository footballGameRepository;
    @Autowired
    FootballPredictionRepository footballPredictionRepository;

    @Test
    public void 트랜잭션_테스트() {
        List<Match> matches = new ArrayList<>();
        for (long i = 0; i < 4; i++) {
            matches.add(new Match(Long.valueOf(i), 0, 0, 0));
        }
        FootballGame footballGame = new FootballGame("2024-02-22", matches, false, false);

        footballGameRepository.save(footballGame);
        String id = footballGame.getId();

        Optional<FootballGame> fg = footballGameRepository.findById(id);

        Assertions.assertThat(fg.isPresent()).isTrue();
    }
}