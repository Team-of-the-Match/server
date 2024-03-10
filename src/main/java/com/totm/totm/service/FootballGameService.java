package com.totm.totm.service;

import com.totm.totm.entity.FootballGame;
import com.totm.totm.entity.FootballPrediction;
import com.totm.totm.entity.Match;
import com.totm.totm.entity.Ranking;
import com.totm.totm.entity.score.FootballScore;
import com.totm.totm.exception.*;
import com.totm.totm.repository.FootballGameRepository;
import com.totm.totm.repository.FootballPredictionRepository;
import com.totm.totm.repository.score.FootballScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.*;
import java.util.stream.Collectors;

import static com.totm.totm.dto.GameDto.*;

@Service
@RequiredArgsConstructor
public class FootballGameService {

    private final MongoTemplate mongoTemplate;
    private final FootballGameRepository footballGameRepository;
    private final FootballPredictionRepository footballPredictionRepository;
    private final FootballScoreRepository footballScoreRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional(value = "mongoTransactionManager", rollbackFor = { MethodArgumentNotValidException.class })
    public void createGame(CreateGameRequestDto request) {
        Optional<FootballGame> findFootballGame = footballGameRepository.findFootballGamesByGameDate(request.getGameDate());
        if(findFootballGame.isPresent()) throw new GameAlreadyExistException("해당 날짜의 경기가 이미 존재합니다.");

        List<Match> matches = new ArrayList<>();
        for(Long match: request.getMatches()) {
            matches.add(new Match(match, 0, 0, 0));
        }
        FootballGame footballGame = new FootballGame(request.getGameDate(), matches, false, false);
        footballGameRepository.save(footballGame);
    }

    public GameResponseDto findFootballGame(String date) {
        Optional<FootballGame> findFootballGames = footballGameRepository.findFootballGamesByGameDate(date);
        if(findFootballGames.isPresent()) {
            return new GameResponseDto(findFootballGames.get());
        } else throw new GameNotFoundException("해당 게임을 찾을 수 없습니다.");
    }

    public List<String> findOpenedDateFootballGame() {
        return footballGameRepository.findFootballGamesByScoreUpdated(false).stream()
                .map(FootballGame::getGameDate)
                .collect(Collectors.toList());
    }

    public List<String> findClosedDateFootballGame(String gameDate) {
        return footballGameRepository.findFootballGamesByGameDateContainingAndClosed(gameDate, true)
                .stream().map(FootballGame::getGameDate)
                .collect(Collectors.toList());
    }

    @Transactional(value = "mongoTransactionManager", rollbackFor = MethodArgumentNotValidException.class)
    public void closeFootballGame(CloseGameRequestDto request) {
        Optional<FootballGame> fg = footballGameRepository.findById(request.getGameId());
        if(fg.isEmpty()) throw new GameNotFoundException("해당 게임을 찾을 수 없습니다.");
        if(fg.get().isClosed()) throw new GameAlreadyClosedException("이미 게임이 닫혔습니다.");
        if(fg.get().getMatches().size() != request.getResults().size()) throw new IllegalStateException("게임 수와 결과 수가 다릅니다.");

        for(int i = 0; i < request.getResults().size(); i++) {
            mongoTemplate.updateMulti(
                    Query.query(
                            new Criteria().andOperator(
                                Criteria.where("footballGame").is(fg.get()),
                                Criteria.where("predictions." + i).is(request.getResults().get(i))
                            )
                    ),
                    new Update().push("scores", 15),
                    FootballPrediction.class
            );

            mongoTemplate.updateMulti(
                    Query.query(
                            new Criteria().andOperator(
                                Criteria.where("footballGame").is(fg.get()),
                                Criteria.where("predictions." + i).ne(request.getResults().get(i))
                            )
                    ),
                    new Update().push("scores", 5),
                    FootballPrediction.class
            );
        }

        mongoTemplate.updateFirst(
                Query.query(
                        Criteria.where("id").is(fg.get().getId())
                ),
                Update.update("closed", true),
                FootballGame.class
        );
    }

    @Transactional
    public void updateScores(String gameId) {
        Optional<FootballGame> fg = footballGameRepository.findById(gameId);
        if(fg.isEmpty()) throw new GameNotFoundException("해당 게임을 찾을 수 없습니다.");
        if(!fg.get().isClosed()) throw new GameNotClosedException("해당 게임이 아직 닫히지 않았습니다.");
        if(fg.get().isScoreUpdated()) throw new ScoreAlreadyUpdatedException("해당 게임에 대한 스코어가 이미 반영됐습니다.");
        List<FootballPrediction> fps = footballPredictionRepository.findByFootballGame(fg.get());

        mongoTemplate.updateFirst(
                Query.query(
                        Criteria.where("id").is(gameId)
                ),
                Update.update("scoreUpdated", true),
                FootballGame.class
        );

        Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<>();
        for (FootballPrediction fp : fps) {
            Optional<FootballScore> fs = footballScoreRepository.findByYearAndMemberId(Integer.parseInt(fg.get().getGameDate().split("-")[0]), fp.getMemberId());
            if (fs.isEmpty()) continue;
            int sum = fp.getScores().stream().mapToInt(s -> s).sum();
            fs.get().updateScore(sum);
            tuples.add(ZSetOperations.TypedTuple.of(new Ranking(fs.get().getMember().getEmail(), fs.get().getMember().getNickname()), (double) fs.get().getScore()));
        }
        redisTemplate.opsForZSet().add(
                "football_" + Integer.parseInt(fg.get().getGameDate().split("-")[0]),
                tuples);
    }

}
