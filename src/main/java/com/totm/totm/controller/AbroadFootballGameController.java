package com.totm.totm.controller;

import com.totm.totm.dto.NormalResponse;
import com.totm.totm.service.AbroadFootballGameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.totm.totm.dto.GameDto.CloseGameRequestDto;
import static com.totm.totm.dto.GameDto.CreateGameRequestDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/abroad-football-game")
public class AbroadFootballGameController {

    private final AbroadFootballGameService abroadFootballGameService;

    @PostMapping("/create")
    public ResponseEntity createAbroadFootballGame(@Valid @RequestBody CreateGameRequestDto request) {
        abroadFootballGameService.createGame(request);
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @GetMapping("/game")
    public ResponseEntity findAbroadFootballGame(String date) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(abroadFootballGameService.findAbroadFootballGame(date)).build());
    }

    @GetMapping("/opened-date")
    public ResponseEntity findOpenedDateAbroadFootballGame() {
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(abroadFootballGameService.findOpenedDateAbroadFootballGame()).build());
    }

    @GetMapping("/closed-date")
    public ResponseEntity findClosedDateAbroadFootballGame(String gameDate) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(abroadFootballGameService.findClosedDateAbroadFootballGame(gameDate)).build());
    }

    @PatchMapping("/close")
    public ResponseEntity closeAbroadFootballGame(@Valid @RequestBody CloseGameRequestDto request) {
        abroadFootballGameService.closeAbroadFootballGame(request);
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @PatchMapping("/update-score")
    public ResponseEntity updateAbroadFootballScore(String gameId) {
        abroadFootballGameService.updateScores(gameId);
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }
}
