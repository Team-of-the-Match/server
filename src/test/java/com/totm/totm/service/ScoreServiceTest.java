package com.totm.totm.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static com.totm.totm.dto.ScoreDto.*;

@SpringBootTest
@Transactional
class ScoreServiceTest {

    @Autowired
    ScoreService scoreService;

    @Test
    @Transactional(readOnly = true)
    public void getFootballRanking() {
        PageRequest pr = PageRequest.of(0, 10);
        Page<FootballRankingResponseDto> result = scoreService.getFootballRanking(2024, pr);
        System.out.println(result);
    }
}