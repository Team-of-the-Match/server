package com.totm.totm.dto;

import com.totm.totm.entity.AbroadFootballGame;
import com.totm.totm.entity.FootballGame;
import com.totm.totm.entity.Match;
import com.totm.totm.entity.Result;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

public class GameDto {

    @Data
    public static class CreateGameRequestDto {
        @NotNull(message = "게임 날짜는 빈값일 수 없습니다.")
        private String gameDate;

        @NotEmpty(message = "게임은 1개 이상 필요합니다.")
        private List<Long> matches;
    }

    @Data
    public static class CloseGameRequestDto {
        @NotNull(message = "게임 아이디는 빈값일 수 없습니다.")
        private String gameId;

        @NotEmpty(message = "결과가 한 개 이상 존재해야합니다.")
        private List<Result> results;
    }

    @Data
    public static class GameResponseDto {
        private String id;
        private String gameDate;
        private List<MatchResponseDto> matches;
        private boolean closed;
        private boolean scoreUpdated;

        public GameResponseDto(FootballGame game) {
            this.id = game.getId();
            this.gameDate = game.getGameDate();
            this.matches = game.getMatches().stream()
                    .map(MatchResponseDto::new)
                    .collect(Collectors.toList());
            this.closed = game.isClosed();
            this.scoreUpdated = game.isScoreUpdated();
        }

        public GameResponseDto(AbroadFootballGame game) {
            this.id = game.getId();
            this.gameDate = game.getGameDate();
            this.matches = game.getMatches().stream()
                    .map(MatchResponseDto::new)
                    .collect(Collectors.toList());
            this.closed = game.isClosed();
            this.scoreUpdated = game.isScoreUpdated();
        }
    }

    @Data
    public static class MatchResponseDto {
        private Long match;
        private int home;
        private int draw;
        private int away;

        public MatchResponseDto(Match match) {
            this.match = match.getMatch();
            this.home = match.getHome();
            this.draw = match.getDraw();
            this.away = match.getAway();
        }
    }
}
