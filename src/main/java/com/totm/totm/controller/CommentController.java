package com.totm.totm.controller;

import com.totm.totm.dto.NormalResponse;
import com.totm.totm.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/reported")
    public ResponseEntity findReportedComments(Pageable pageable) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(commentService.findReportedComments(pageable)).build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data("success").build());
    }
}
