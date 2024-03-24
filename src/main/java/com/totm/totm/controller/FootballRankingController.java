package com.totm.totm.controller;

import com.totm.totm.service.FootballRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/football-ranking")
public class FootballRankingController {

    private final FootballRankingService footballRankingService;

    @GetMapping("/manager")
    public ResponseEntity getRankingForManager(int year, int page) {
        return ResponseEntity.ok(footballRankingService.getRankingForManager(year, page));
    }

    @GetMapping("/client")
    public ResponseEntity getRankingForClient(String email, String nickname, int year) {
        return ResponseEntity.ok(footballRankingService.getRankingForClient(email, nickname, year));
    }
}
