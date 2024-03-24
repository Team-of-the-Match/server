package com.totm.totm.controller;

import com.totm.totm.service.FootballPredictionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.totm.totm.dto.PredictionDto.PredictGameRequestDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/football-prediction")
public class FootballPredictionController {

    private final FootballPredictionService footballPredictionService;

    @GetMapping("/opened")
    public ResponseEntity findOpenedFootballPrediction(Long memberId, String gameDate) {
        return ResponseEntity.ok(footballPredictionService.findOpened(memberId, gameDate));
    }

    @GetMapping("/closed")
    public ResponseEntity findClosedFootballPrediction(Long memberId, String gameDate) {
        return ResponseEntity.ok(footballPredictionService.findClosed(memberId, gameDate));
    }

    @PostMapping("/predict")
    public ResponseEntity predict(@Valid @RequestBody PredictGameRequestDto request) {
        footballPredictionService.predict(request);
        return ResponseEntity.ok().build();
    }

}
