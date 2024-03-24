package com.totm.totm.service;

import com.totm.totm.entity.FootballGame;
import com.totm.totm.entity.FootballPrediction;
import com.totm.totm.entity.Prediction;
import com.totm.totm.exception.GameNotFoundException;
import com.totm.totm.exception.MemberNotFoundException;
import com.totm.totm.exception.PredictionAlreadyExistException;
import com.totm.totm.exception.PredictionNotFoundException;
import com.totm.totm.repository.FootballGameRepository;
import com.totm.totm.repository.FootballPredictionRepository;
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
public class FootballPredictionService {

    private final MemberRepository memberRepository;
    private final FootballGameRepository footballGameRepository;
    private final FootballPredictionRepository footballPredictionRepository;
    private final MongoTemplate mongoTemplate;

    @Transactional(value = "mongoTransactionManager", rollbackFor = MethodArgumentNotValidException.class)
    public void predict(PredictGameRequestDto request) {
        if(memberRepository.findById(request.getMemberId()).isEmpty())
            throw new MemberNotFoundException("해당 멤버를 찾을 수 없습니다.");

        Optional<FootballGame> findFootballGame = footballGameRepository.findById(request.getGameId());
        if(findFootballGame.isEmpty()) throw new GameNotFoundException("해당 게임을 찾을 수 없습니다.");

        if(!LocalDate.parse(findFootballGame.get().getGameDate()).isAfter(LocalDate.now()))
            throw new IllegalStateException("예측 기간이 지났습니다.");

        Optional<FootballPrediction> findFootballPrediction = footballPredictionRepository.findByFootballGameAndMemberId(findFootballGame.get(), request.getMemberId());
        if(findFootballPrediction.isPresent()) throw new PredictionAlreadyExistException("이미 예측이 존재합니다.");

        FootballPrediction fp = new FootballPrediction(request.getMemberId(), request.getPredictions(), findFootballGame.get());
        footballPredictionRepository.save(fp);

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
                FootballGame.class
        );
    }

    public OpenGamePredictionResponseDto findOpened(Long memberId, String gameDate) {
        Optional<FootballGame> fg = footballGameRepository.findByGameDateAndClosed(gameDate, false);
        if(fg.isEmpty()) throw new GameNotFoundException("해당 날짜에 열린 경기가 없습니다.");
        Optional<FootballPrediction> fp = footballPredictionRepository.findByFootballGameAndMemberId(fg.get(), memberId);
        return new OpenGamePredictionResponseDto(fg.get(), fp.orElse(null));
    }

    public CloseGamePredictionResponseDto findClosed(Long memberId, String gameDate) {
        Optional<FootballGame> fg = footballGameRepository.findByGameDateAndClosed(gameDate, true);
        if(fg.isEmpty()) throw new GameNotFoundException("해당 날짜에 닫힌 경기가 없습니다.");
        Optional<FootballPrediction> fp = footballPredictionRepository.findByFootballGameAndMemberId(fg.get(), memberId);
        if(fp.isEmpty()) throw new PredictionNotFoundException("해당 날짜에 대한 예측이 없습니다.");
        return new CloseGamePredictionResponseDto(fg.get(), fp.get());
    }
}
