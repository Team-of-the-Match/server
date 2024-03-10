package com.totm.totm.controller;

import com.totm.totm.dto.NormalResponse;
import com.totm.totm.service.AbroadFootballPredictionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
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
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(abroadFootballPredictionService.findOpened(memberId, gameDate)).build());
    }

    @GetMapping("/closed")
    public ResponseEntity findClosedAbroadFootballPrediction(Long memberId, String gameDate) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(abroadFootballPredictionService.findClosed(memberId, gameDate)).build());
    }

    @PostMapping("/predict")
    public ResponseEntity predict(@Valid @RequestBody PredictGameRequestDto request) {
        abroadFootballPredictionService.predict(request);
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

}
