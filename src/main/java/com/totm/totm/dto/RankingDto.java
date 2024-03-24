package com.totm.totm.dto;

import lombok.Data;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RankingDto {

    @Data
    public static class RankingManagerResponseDto {
        private String email;
        private String nickname;
        private int score;

        public RankingManagerResponseDto(ZSetOperations.TypedTuple<Object> typedTuple) {
            LinkedHashMap m = (LinkedHashMap) typedTuple.getValue();
            this.email = (String) m.get("email");
            this.nickname = (String) m.get("nickname");
            this.score = typedTuple.getScore().intValue();
        }
    }

    @Data
    public static class RankingClientResponseDto {
        private int score;
        private Long rank;
        private Long participants;
        private List<RankingMemberResponseDto> members;

        public RankingClientResponseDto(int score, Long rank, Long participants, Set<ZSetOperations.TypedTuple<Object>> typedTupleSet) {
            this.score = score;
            this.rank = rank;
            this.participants = participants;
            this.members = typedTupleSet.stream().map(RankingMemberResponseDto::new).collect(Collectors.toList());
        }
    }

    @Data
    public static class RankingMemberResponseDto {
        private String nickname;
        private int score;

        public RankingMemberResponseDto(ZSetOperations.TypedTuple<Object> typedTuple) {
            LinkedHashMap m = (LinkedHashMap) typedTuple.getValue();
            this.nickname = (String) m.get("nickname");
            System.out.println("typedTuple = " + typedTuple.getScore().intValue());
            this.score = typedTuple.getScore().intValue();
        }
    }
}
