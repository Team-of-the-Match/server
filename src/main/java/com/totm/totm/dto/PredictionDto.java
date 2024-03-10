package com.totm.totm.dto;

import com.totm.totm.entity.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

public class PredictionDto {

    @Data
    public static class PredictGameRequestDto {
        @NotNull(message = "멤버 아이디가 없습니다.")
        private Long memberId;
        @NotNull(message = "게임 아이디가 없습니다.")
        private String gameId;
        @NotEmpty(message = "예측이 입력되지 않았습니다.")
        private List<Prediction> predictions;
    }

    @Data
    public static class OpenGamePredictionResponseDto {
        private String gameId;
        private String gameDate;
        private List<OpenMatchResponseDto> matches;
        private String predictionId;
        private List<Prediction> predictions;

        public OpenGamePredictionResponseDto(FootballGame fg, FootballPrediction fp) {
            this.gameId = fg.getId();
            this.gameDate = fg.getGameDate();
            this.matches = fg.getMatches().stream().map(OpenMatchResponseDto::new).collect(Collectors.toList());
            if(fp != null) {
                this.predictionId = fp.getId();
                this.predictions = fp.getPredictions();
            }
        }

        public OpenGamePredictionResponseDto(AbroadFootballGame afg, AbroadFootballPrediction afp) {
            this.gameId = afg.getId();
            this.gameDate = afg.getGameDate();
            this.matches = afg.getMatches().stream().map(OpenMatchResponseDto::new).collect(Collectors.toList());
            if(afp != null) {
                this.predictionId = afp.getId();
                this.predictions = afp.getPredictions();
            }
        }
    }

    @Data
    public static class OpenMatchResponseDto {
        private Long match;

        public OpenMatchResponseDto(Match match) {
            this.match = match.getMatch();
        }
    }

    @Data
    public static class CloseGamePredictionResponseDto {
        private String gameId;
        private String gameDate;
        private List<CloseMatchResponseDto> matches;
        private String predictionId;
        private List<Prediction> predictions;
        private List<Integer> scores;

        public CloseGamePredictionResponseDto(FootballGame fg, FootballPrediction fp) {
            this.gameId = fg.getId();
            this.gameDate = fg.getGameDate();
            this.matches = fg.getMatches().stream().map(CloseMatchResponseDto::new).collect(Collectors.toList());
            this.predictionId = fp.getId();
            this.predictions = fp.getPredictions();
            this.scores = fp.getScores();
        }

        public CloseGamePredictionResponseDto(AbroadFootballGame fg, AbroadFootballPrediction fp) {
            this.gameId = fg.getId();
            this.gameDate = fg.getGameDate();
            this.matches = fg.getMatches().stream().map(CloseMatchResponseDto::new).collect(Collectors.toList());
            this.predictionId = fp.getId();
            this.predictions = fp.getPredictions();
            this.scores = fp.getScores();
        }
    }

    @Data
    public static class CloseMatchResponseDto {
        private Long match;
        private int home;
        private int draw;
        private int away;

        public CloseMatchResponseDto(Match match) {
            this.match = match.getMatch();
            this.home = match.getHome();
            this.draw = match.getDraw();
            this.away = match.getAway();
        }
    }
}
