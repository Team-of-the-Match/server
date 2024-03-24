package com.totm.totm.controller;

import com.totm.totm.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.totm.totm.dto.CommentDto.AddCommentRequestDto;
import static com.totm.totm.dto.CommentDto.DeleteCommentRequestDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/add")
    public ResponseEntity addComment(@RequestBody AddCommentRequestDto request) {
        commentService.addComment(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/comments")
    public ResponseEntity findCommentsByPost(Long postId, @RequestParam(required = false) Long lastCommentId) {
        return ResponseEntity.ok(commentService.findCommentsByPost(postId, lastCommentId));
    }
    
    @PatchMapping("/report/{id}")
    public ResponseEntity reportComment(@PathVariable Long id) {
        commentService.reportComment(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/normalize/{id}")
    public ResponseEntity normalizeComment(@PathVariable Long id) {
        commentService.normalizeComment(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reported")
    public ResponseEntity findReportedComments(Pageable pageable) {
        return ResponseEntity.ok(commentService.findReportedComments(pageable));
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteComment(@Valid @RequestBody DeleteCommentRequestDto request) {
        commentService.deleteComment(request);
        return ResponseEntity.ok().build();
    }
}
