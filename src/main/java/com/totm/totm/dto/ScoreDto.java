package com.totm.totm.dto;

import com.totm.totm.entity.BaseballScore;
import com.totm.totm.entity.BasketballScore;
import com.totm.totm.entity.FootballScore;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public class ScoreDto {

    @Data
    public static class FootballRankingResponseDto {
        private int score;
        private String username;
        private String nickname;
        private String name;
        private String phoneNumber;
        private LocalDateTime createdDate;

        public FootballRankingResponseDto(FootballScore footballScore) {
            this.score = footballScore.getScore();
            this.username = footballScore.getMember().getUsername();
            this.nickname = footballScore.getMember().getNickname();
            this.name = footballScore.getMember().getName();
            this.phoneNumber = footballScore.getMember().getPhoneNumber();
            this.createdDate = footballScore.getMember().getCreatedDate();
        }
    }

    @Data
    public static class BaseballRankingResponseDto {
        private int score;
        private String username;
        private String nickname;
        private String name;
        private String phoneNumber;
        private LocalDateTime createdDate;

        public BaseballRankingResponseDto(BaseballScore baseballScore) {
            this.score = baseballScore.getScore();
            this.username = baseballScore.getMember().getUsername();
            this.nickname = baseballScore.getMember().getNickname();
            this.name = baseballScore.getMember().getName();
            this.phoneNumber = baseballScore.getMember().getPhoneNumber();
            this.createdDate = baseballScore.getMember().getCreatedDate();
        }
    }

    @Data
    public static class BasketballRankingResponseDto {
        private int score;
        private String username;
        private String nickname;
        private String name;
        private String phoneNumber;
        private LocalDateTime createdDate;

        public BasketballRankingResponseDto(BasketballScore basketballScore) {
            this.score = basketballScore.getScore();
            this.username = basketballScore.getMember().getUsername();
            this.nickname = basketballScore.getMember().getNickname();
            this.name = basketballScore.getMember().getName();
            this.phoneNumber = basketballScore.getMember().getPhoneNumber();
            this.createdDate = basketballScore.getMember().getCreatedDate();
        }
    }

    @Data
    public static class MyFootballRankingResponseDto {
        private int myScore;
        private int participants;
        private int rank;
        private Page<FootballRankResponseDto> rankResponse;

        public MyFootballRankingResponseDto(int myScore, int participants, int rank, Page<FootballScore> score) {
            this.myScore = myScore;
            this.participants = participants;
            this.rank = rank;
            this.rankResponse = score.map(FootballRankResponseDto::new);
        }
    }

    @Data
    public static class FootballRankResponseDto {
        private String nickname;
        private int score;

        public FootballRankResponseDto(FootballScore footballScore) {
            this.nickname = footballScore.getMember().getNickname();
            this.score = footballScore.getScore();
        }
    }

    @Data
    public static class MyBaseballRankingResponseDto {
        private int myScore;
        private int participants;
        private int rank;
        private Page<BaseballRankResponseDto> rankResponse;

        public MyBaseballRankingResponseDto(int myScore, int participants, int rank, Page<BaseballScore> score) {
            this.myScore = myScore;
            this.participants = participants;
            this.rank = rank;
            this.rankResponse = score.map(BaseballRankResponseDto::new);
        }
    }

    @Data
    public static class BaseballRankResponseDto {
        private String nickname;
        private int score;

        public BaseballRankResponseDto(BaseballScore baseballScore) {
            this.nickname = baseballScore.getMember().getNickname();
            this.score = baseballScore.getScore();
        }
    }

    @Data
    public static class MyBasketballRankingResponseDto {
        private int myScore;
        private int participants;
        private int rank;
        private Page<BasketballRankResponseDto> rankResponse;

        public MyBasketballRankingResponseDto(int myScore, int participants, int rank, Page<BasketballScore> score) {
            this.myScore = myScore;
            this.participants = participants;
            this.rank = rank;
            this.rankResponse = score.map(BasketballRankResponseDto::new);
        }
    }

    @Data
    public static class BasketballRankResponseDto {
        private String nickname;
        private int score;

        public BasketballRankResponseDto(BasketballScore basketballScore) {
            this.nickname = basketballScore.getMember().getNickname();
            this.score = basketballScore.getScore();
        }
    }
}
