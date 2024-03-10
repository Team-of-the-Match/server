package com.totm.totm.service;

import com.totm.totm.entity.*;
import com.totm.totm.exception.GameNotFoundException;
import com.totm.totm.exception.MemberNotFoundException;
import com.totm.totm.exception.PredictionAlreadyExistException;
import com.totm.totm.exception.PredictionNotFoundException;
import com.totm.totm.repository.AbroadFootballGameRepository;
import com.totm.totm.repository.AbroadFootballPredictionRepository;
import com.totm.totm.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
import java.util.Optional;

import static com.totm.totm.dto.PredictionDto.*;

@Service
@RequiredArgsConstructor
public class AbroadFootballPredictionService {

    private final MemberRepository memberRepository;
    private final AbroadFootballGameRepository abroadFootballGameRepository;
    private final AbroadFootballPredictionRepository abroadFootballPredictionRepository;
    private final MongoTemplate mongoTemplate;

    @Transactional(value = "mongoTransactionManager", rollbackFor = MethodArgumentNotValidException.class)
    public void predict(PredictGameRequestDto request) {
        if(memberRepository.findById(request.getMemberId()).isEmpty())
            throw new MemberNotFoundException("해당 멤버를 찾을 수 없습니다.");

        Optional<AbroadFootballGame> findAbroadFootballGame = abroadFootballGameRepository.findById(request.getGameId());
        if(findAbroadFootballGame.isEmpty()) throw new GameNotFoundException("해당 게임을 찾을 수 없습니다.");

        if(!LocalDate.parse(findAbroadFootballGame.get().getGameDate()).isAfter(LocalDate.now()))
            throw new IllegalStateException("예측 기간이 지났습니다.");

        Optional<AbroadFootballPrediction> findAbroadFootballPrediction = abroadFootballPredictionRepository.findByAbroadFootballGameAndMemberId(findAbroadFootballGame.get(), request.getMemberId());
        if(findAbroadFootballPrediction.isPresent()) throw new PredictionAlreadyExistException("이미 예측이 존재합니다.");

        AbroadFootballPrediction afp = new AbroadFootballPrediction(request.getMemberId(), request.getPredictions(), findAbroadFootballGame.get());
        abroadFootballPredictionRepository.save(afp);

        Update update = new Update();
        for(int i = 0; i < request.getPredictions().size(); i++) {
            String value = null;
            if(request.getPredictions().get(i) == Prediction.HOME) value = ".home";
            if(request.getPredictions().get(i) == Prediction.AWAY) value = ".away";
            if(request.getPredictions().get(i) == Prediction.DRAW) value = ".draw";
            update.inc("matches." + i + value, 1);
        }
        mongoTemplate.findAndModify(
                Query.query(
                        Criteria.where("id").is(request.getGameId())
                ),
                update,
                AbroadFootballGame.class
        );
    }

    public OpenGamePredictionResponseDto findOpened(Long memberId, String gameDate) {
        Optional<AbroadFootballGame> afg = abroadFootballGameRepository.findByGameDateAndClosed(gameDate, false);
        if(afg.isEmpty()) throw new GameNotFoundException("해당 날짜에 열린 경기가 없습니다.");
        Optional<AbroadFootballPrediction> afp = abroadFootballPredictionRepository.findByAbroadFootballGameAndMemberId(afg.get(), memberId);
        return new OpenGamePredictionResponseDto(afg.get(), afp.orElse(null));
    }

    public CloseGamePredictionResponseDto findClosed(Long memberId, String gameDate) {
        Optional<AbroadFootballGame> afg = abroadFootballGameRepository.findByGameDateAndClosed(gameDate, true);
        if(afg.isEmpty()) throw new GameNotFoundException("해당 날짜에 닫힌 경기가 없습니다.");
        Optional<AbroadFootballPrediction> afp = abroadFootballPredictionRepository.findByAbroadFootballGameAndMemberId(afg.get(), memberId);
        if(afp.isEmpty()) throw new PredictionNotFoundException("해당 날짜에 대한 예측이 없습니다.");
        return new CloseGamePredictionResponseDto(afg.get(), afp.get());
    }
}
