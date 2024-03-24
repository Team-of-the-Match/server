package com.totm.totm.service;

import com.totm.totm.entity.*;
import com.totm.totm.repository.FootballGameRepository;
import com.totm.totm.repository.FootballPredictionRepository;
import com.totm.totm.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
@Rollback(value = false)
class FootballGameServiceTest {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    FootballGameRepository footballGameRepository;
    @Autowired
    FootballPredictionRepository footballPredictionRepository;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    EntityManager em;

    @BeforeEach
    public void beforeEach() {
        int number = 100;

        for(int i = 0; i < number; i++) {
            Member member = new Member("jakey1110" + i + "@naver.com", "qwerty", "닉네임" + i, true);
            memberRepository.save(member);
        }

        em.flush();

        List<Match> matches = new ArrayList<>();
        for(long i = 0; i < 5L; i++) {
            matches.add(new Match(i, 0, 0, 0));
        }
        FootballGame footballGame = new FootballGame("2024-03-16", matches, false, false);
        footballGameRepository.save(footballGame);

        List<Prediction> predictions = new ArrayList<>();
        predictions.add(Prediction.DRAW);
        predictions.add(Prediction.HOME);
        predictions.add(Prediction.DRAW);
        predictions.add(Prediction.AWAY);
        predictions.add(Prediction.DRAW);
        for(long i = 0; i < number; i++) {
            FootballPrediction fp = new FootballPrediction(i, predictions, footballGame);
            footballPredictionRepository.save(fp);
        }
    }

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

    @Test
    public void 게임_닫기_스코어_성능() {
        FootballGame fg = footballGameRepository.findFootballGameByGameDate("2024-03-16").get();

        List<Result> results = new ArrayList<>();
        results.add(Result.DRAW);
        results.add(Result.HOME);
        results.add(Result.HOME);
        results.add(Result.AWAY);
        results.add(Result.AWAY);
        for(int i = 0; i < results.size(); i++) {
            mongoTemplate.updateMulti(
                    Query.query(
                            new Criteria().andOperator(
                                    Criteria.where("footballGame").is(fg),
                                    Criteria.where("predictions." + i).is(results.get(i))
                            )
                    ),
                    new Update().push("scores", 15),
                    FootballPrediction.class
            );

            mongoTemplate.updateMulti(
                    Query.query(
                            new Criteria().andOperator(
                                    Criteria.where("footballGame").is(fg),
                                    Criteria.where("predictions." + i).ne(results.get(i))
                            )
                    ),
                    new Update().push("scores", 5),
                    FootballPrediction.class
            );
        }

        List<FootballPrediction> fps = footballPredictionRepository.findByFootballGame(fg);

        mongoTemplate.updateFirst(
                Query.query(
                        Criteria.where("id").is(fg.getId())
                ),
                Update.update("closed", true),
                FootballGame.class
        );

        mongoTemplate.updateFirst(
                Query.query(
                        Criteria.where("id").is(fg.getId())
                ),
                Update.update("scoreUpdated", true),
                FootballGame.class
        );

        for (FootballPrediction fp : fps) {
            int sum = fp.getScores().stream().mapToInt(s -> s).sum();

            Optional<Member> m = memberRepository.findById(fp.getMemberId());
            if(m.isEmpty()) continue;

            redisTemplate.opsForZSet().incrementScore(
                    "football_" + fg.getGameDate().split("-")[0],
                    new Ranking(m.get().getEmail(), m.get().getNickname()),
                    sum
            );
        }
    }
}