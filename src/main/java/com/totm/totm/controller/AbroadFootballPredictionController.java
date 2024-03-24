package com.totm.totm.controller;

import com.totm.totm.service.AbroadFootballPredictionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.totm.totm.dto.PredictionDto.PredictGameRequestDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/abroad-football-prediction")
public class AbroadFootballPredictionController {

    private final AbroadFootballPredictionService abroadFootballPredictionService;

    @GetMapping("/opened")
    public ResponseEntity findOpenedAbroadFootballPrediction(Long memberId, String gameDate) {
        return ResponseEntity.ok(abroadFootballPredictionService.findOpened(memberId, gameDate));
    }

    @GetMapping("/closed")
    public ResponseEntity findClosedAbroadFootballPrediction(Long memberId, String gameDate) {
        return ResponseEntity.ok(abroadFootballPredictionService.findClosed(memberId, gameDate));
    }

    @PostMapping("/predict")
    public ResponseEntity predict(@Valid @RequestBody PredictGameRequestDto request) {
        abroadFootballPredictionService.predict(request);
        return ResponseEntity.ok().build();
    }

}
