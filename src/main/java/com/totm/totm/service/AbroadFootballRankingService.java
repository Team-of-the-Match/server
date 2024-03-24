package com.totm.totm.service;

import com.totm.totm.entity.Ranking;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.totm.totm.dto.RankingDto.RankingClientResponseDto;
import static com.totm.totm.dto.RankingDto.RankingManagerResponseDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, rollbackFor = MethodArgumentNotValidException.class)
public class AbroadFootballRankingService {

    private final RedisTemplate<String, Object> redisTemplate;

    public List<RankingManagerResponseDto> getRankingForManager(int year, int page) {
        return redisTemplate.opsForZSet().reverseRangeWithScores("abroad_football_" + year, page * 10, page * 10 + 10)
                .stream().map(RankingManagerResponseDto::new).collect(Collectors.toList());
    }

    public RankingClientResponseDto getRankingForClient(String email, String nickname, int year) {
        Ranking ranking = new Ranking(email, nickname);
        int score = redisTemplate.opsForZSet().score("abroad_football_" + year, ranking).intValue();
        Long participants = redisTemplate.opsForZSet().count("abroad_football_" + year, 0, Double.MAX_VALUE);
        Long rank = redisTemplate.opsForZSet().count("abroad_football_" + year, score + 1, Double.MAX_VALUE) + 1;
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet().reverseRangeWithScores("abroad_football_" + year, 0, 10);
        return new RankingClientResponseDto(score, rank, participants, typedTuples);
    }
}
