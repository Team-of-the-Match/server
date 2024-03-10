package com.totm.totm.controller;

import com.totm.totm.dto.NormalResponse;
import com.totm.totm.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.totm.totm.dto.CommentDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/add")
    public ResponseEntity addComment(@RequestBody AddCommentRequestDto request) {
        commentService.addComment(request);
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @GetMapping("/comments")
    public ResponseEntity findCommentsByPost(Long postId, @RequestParam(required = false) Long lastCommentId) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(commentService.findCommentsByPost(postId, lastCommentId)).build());
    }
    
    @PatchMapping("/report/{id}")
    public ResponseEntity reportComment(@PathVariable Long id) {
        commentService.reportComment(id);
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @PatchMapping("/normalize/{id}")
    public ResponseEntity normalizeComment(@PathVariable Long id) {
        commentService.normalizeComment(id);
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }

    @GetMapping("/reported")
    public ResponseEntity findReportedComments(Pageable pageable) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(commentService.findReportedComments(pageable)).build());
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteComment(@Valid @RequestBody DeleteCommentRequestDto request) {
        commentService.deleteComment(request);
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).build());
    }
}
