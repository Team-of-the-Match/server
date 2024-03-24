package com.totm.totm.controller;

import com.totm.totm.service.FootballGameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.totm.totm.dto.GameDto.CloseGameRequestDto;
import static com.totm.totm.dto.GameDto.CreateGameRequestDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/football-game")
public class FootballGameController {

    private final FootballGameService footballGameService;

    @PostMapping("/create")
    public ResponseEntity createFootballGame(@Valid @RequestBody CreateGameRequestDto request) {
        footballGameService.createGame(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/game")
    public ResponseEntity findFootballGame(String date) {
        return ResponseEntity.ok(footballGameService.findFootballGame(date));
    }

    @GetMapping("/opened-date")
    public ResponseEntity findOpenedDateFootballGame() {
        return ResponseEntity.ok(footballGameService.findOpenedDateFootballGame());
    }

    @GetMapping("/closed-date")
    public ResponseEntity findClosedDateFootballGame(String gameDate) {
        return ResponseEntity.ok(footballGameService.findClosedDateFootballGame(gameDate));
    }

    @PatchMapping("/close")
    public ResponseEntity closeFootballGame(@Valid @RequestBody CloseGameRequestDto request) {
        footballGameService.closeFootballGame(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update-score")
    public ResponseEntity updateFootballScore(String gameId) {
        footballGameService.updateScores(gameId);
        return ResponseEntity.ok().build();
    }
}
