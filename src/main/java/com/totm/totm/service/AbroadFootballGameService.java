package com.totm.totm.service;

import com.totm.totm.entity.AbroadFootballGame;
import com.totm.totm.entity.AbroadFootballPrediction;
import com.totm.totm.entity.Match;
import com.totm.totm.entity.Ranking;
import com.totm.totm.entity.score.AbroadFootballScore;
import com.totm.totm.exception.*;
import com.totm.totm.repository.AbroadFootballGameRepository;
import com.totm.totm.repository.AbroadFootballPredictionRepository;
import com.totm.totm.repository.score.AbroadFootballScoreRepository;
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
public class AbroadFootballGameService {

    private final MongoTemplate mongoTemplate;
    private final AbroadFootballGameRepository abroadFootballGameRepository;
    private final AbroadFootballPredictionRepository abroadFootballPredictionRepository;
    private final AbroadFootballScoreRepository abroadFootballScoreRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional(value = "mongoTransactionManager", rollbackFor = { MethodArgumentNotValidException.class })
    public void createGame(CreateGameRequestDto request) {
        Optional<AbroadFootballGame> findAbroadFootballGame = abroadFootballGameRepository.findAbroadFootballGamesByGameDate(request.getGameDate());
        if(findAbroadFootballGame.isPresent()) throw new GameAlreadyExistException("해당 날짜의 경기가 이미 존재합니다.");

        List<Match> matches = new ArrayList<>();
        for(Long match: request.getMatches()) {
            matches.add(new Match(match, 0, 0, 0));
        }
        AbroadFootballGame abroadFootballGame = new AbroadFootballGame(request.getGameDate(), matches, false, false);
        abroadFootballGameRepository.save(abroadFootballGame);
    }

    public GameResponseDto findAbroadFootballGame(String date) {
        Optional<AbroadFootballGame> findAbroadFootballGames = abroadFootballGameRepository.findAbroadFootballGamesByGameDate(date);
        if(findAbroadFootballGames.isPresent()) {
            return new GameResponseDto(findAbroadFootballGames.get());
        } else throw new GameNotFoundException("해당 게임을 찾을 수 없습니다.");
    }

    public List<String> findOpenedDateAbroadFootballGame() {
        return abroadFootballGameRepository.findAbroadFootballGamesByScoreUpdated(false).stream()
                .map(AbroadFootballGame::getGameDate)
                .collect(Collectors.toList());
    }

    public List<String> findClosedDateAbroadFootballGame(String gameDate) {
        return abroadFootballGameRepository.findAbroadFootballGamesByGameDateContainingAndClosed(gameDate, true)
                .stream().map(AbroadFootballGame::getGameDate)
                .collect(Collectors.toList());
    }

    @Transactional(value = "mongoTransactionManager", rollbackFor = MethodArgumentNotValidException.class)
    public void closeAbroadFootballGame(CloseGameRequestDto request) {
        Optional<AbroadFootballGame> afg = abroadFootballGameRepository.findById(request.getGameId());
        if(afg.isEmpty()) throw new GameNotFoundException("해당 게임을 찾을 수 없습니다.");
        if(afg.get().isClosed()) throw new GameAlreadyClosedException("이미 게임이 닫혔습니다.");
        if(afg.get().getMatches().size() != request.getResults().size()) throw new IllegalStateException("게임 수와 결과 수가 다릅니다.");

        for(int i = 0; i < request.getResults().size(); i++) {
            mongoTemplate.updateMulti(
                    Query.query(
                            new Criteria().andOperator(
                                Criteria.where("abroadFootballGame").is(afg.get()),
                                Criteria.where("predictions." + i).is(request.getResults().get(i))
                            )
                    ),
                    new Update().push("scores", 15),
                    AbroadFootballPrediction.class
            );

            mongoTemplate.updateMulti(
                    Query.query(
                            new Criteria().andOperator(
                                Criteria.where("abroadFootballGame").is(afg.get()),
                                Criteria.where("predictions." + i).ne(request.getResults().get(i))
                            )
                    ),
                    new Update().push("scores", 5),
                    AbroadFootballPrediction.class
            );
        }

        mongoTemplate.updateFirst(
                Query.query(
                        Criteria.where("id").is(afg.get().getId())
                ),
                Update.update("closed", true),
                AbroadFootballGame.class
        );
    }

    @Transactional
    public void updateScores(String gameId) {
        Optional<AbroadFootballGame> afg = abroadFootballGameRepository.findById(gameId);
        if(afg.isEmpty()) throw new GameNotFoundException("해당 게임을 찾을 수 없습니다.");
        if(!afg.get().isClosed()) throw new GameNotClosedException("해당 게임이 아직 닫히지 않았습니다.");
        if(afg.get().isScoreUpdated()) throw new ScoreAlreadyUpdatedException("해당 게임에 대한 스코어가 이미 반영됐습니다.");
        List<AbroadFootballPrediction> afps = abroadFootballPredictionRepository.findByAbroadFootballGame(afg.get());

        mongoTemplate.updateFirst(
                Query.query(
                        Criteria.where("id").is(gameId)
                ),
                Update.update("scoreUpdated", true),
                AbroadFootballGame.class
        );

        Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<>();
        for (AbroadFootballPrediction afp : afps) {
            Optional<AbroadFootballScore> afs = abroadFootballScoreRepository.findByYearAndMemberId(Integer.parseInt(afg.get().getGameDate().split("-")[0]), afp.getMemberId());
            if (afs.isEmpty()) continue;
            int sum = afp.getScores().stream().mapToInt(s -> s).sum();
            afs.get().updateScore(sum);
            tuples.add(ZSetOperations.TypedTuple.of(new Ranking(afs.get().getMember().getEmail(), afs.get().getMember().getNickname()), (double) afs.get().getScore()));
        }
        redisTemplate.opsForZSet().add(
                "abroad_football_" + Integer.parseInt(afg.get().getGameDate().split("-")[0]),
                tuples);
    }

}
