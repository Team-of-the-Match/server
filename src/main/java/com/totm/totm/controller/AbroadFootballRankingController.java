package com.totm.totm.controller;

import com.totm.totm.dto.NormalResponse;
import com.totm.totm.service.AbroadFootballRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/abroad-football-ranking")
public class AbroadFootballRankingController {

    private final AbroadFootballRankingService abroadFootballRankingService;

    @GetMapping("/manager")
    public ResponseEntity getRankingForManager(int year, int page) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(abroadFootballRankingService.getRankingForManager(year, page)).build());
    }

    @GetMapping("/client")
    public ResponseEntity getRankingForClient(String email, String nickname, int year) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(abroadFootballRankingService.getRankingForClient(email, nickname, year)).build());
    }
}
