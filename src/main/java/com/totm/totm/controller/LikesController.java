package com.totm.totm.controller;

import com.totm.totm.service.LikesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.totm.totm.dto.LikesDto.LikeRequestDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/like")
    public ResponseEntity like(@Valid @RequestBody LikeRequestDto request) {
        return ResponseEntity.ok(likesService.like(request));
    }

    @PostMapping("/dislike")
    public ResponseEntity dislike(@Valid @RequestBody LikeRequestDto request) {
        return ResponseEntity.ok(likesService.dislike(request));
    }
}
