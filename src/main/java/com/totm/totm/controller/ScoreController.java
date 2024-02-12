package com.totm.totm.controller;

import com.totm.totm.dto.NormalResponse;
import com.totm.totm.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/score")
public class ScoreController {

    private final ScoreService scoreService;

    @GetMapping("/football/ranking")
    public ResponseEntity getFootballRanking(int year, Pageable pageable) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder()
                                .status(200)
                                .data(scoreService.getFootballRanking(year, pageable))
                                .build());
    }

    @GetMapping("/baseball/ranking")
    public ResponseEntity getBaseballRanking(int year, Pageable pageable) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(scoreService.getBaseballRanking(year, pageable)).build());
    }

    @GetMapping("/basketball/ranking")
    public ResponseEntity getBasketballRanking(int year, Pageable pageable) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(scoreService.getBasketballRanking(year, pageable)).build());
    }

    @GetMapping("/football/my")
    public ResponseEntity getMyFootballRanking(Long id, int year) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder()
                        .status(200)
                        .data(scoreService.getMyFootballRanking(id, year))
                        .build());
    }

    @GetMapping("/baseball/my")
    public ResponseEntity getMyBaseballRanking(Long id, int year) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder()
                        .status(200)
                        .data(scoreService.getMyBaseballRanking(id, year))
                        .build());
    }

    @GetMapping("/basketball/my")
    public ResponseEntity getMyBasketballRanking(Long id, int year) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder()
                        .status(200)
                        .data(scoreService.getMyBasketballRanking(id, year))
                        .build());
    }
}
