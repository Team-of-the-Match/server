package com.totm.totm.controller;

import com.totm.totm.dto.NormalResponse;
import com.totm.totm.service.FootballPredictionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
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
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(footballPredictionService.findOpened(memberId, gameDate)).build());
    }

    @GetMapping("/closed")
    public ResponseEntity findClosedFootballPrediction(Long memberId, String gameDate) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(footballPredictionService.findClosed(memberId, gameDate)).build());
    }

    @PostMapping("/predict")
    public ResponseEntity predict(@Valid @RequestBody PredictGameRequestDto request) {
        footballPredictionService.predict(request);
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

}
