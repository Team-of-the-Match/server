package com.totm.totm.controller;

import com.totm.totm.service.AbroadFootballGameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
        return ResponseEntity.ok().build();
    }

    @GetMapping("/game")
    public ResponseEntity findAbroadFootballGame(String date) {
        return ResponseEntity.ok(abroadFootballGameService.findAbroadFootballGame(date));
    }

    @GetMapping("/opened-date")
    public ResponseEntity findOpenedDateAbroadFootballGame() {
        return ResponseEntity.ok(abroadFootballGameService.findOpenedDateAbroadFootballGame());
    }

    @GetMapping("/closed-date")
    public ResponseEntity findClosedDateAbroadFootballGame(String gameDate) {
        return ResponseEntity.ok(abroadFootballGameService.findClosedDateAbroadFootballGame(gameDate));
    }

    @PatchMapping("/close")
    public ResponseEntity closeAbroadFootballGame(@Valid @RequestBody CloseGameRequestDto request) {
        abroadFootballGameService.closeAbroadFootballGame(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update-score")
    public ResponseEntity updateAbroadFootballScore(String gameId) {
        abroadFootballGameService.updateScores(gameId);
        return ResponseEntity.ok().build();
    }
}
