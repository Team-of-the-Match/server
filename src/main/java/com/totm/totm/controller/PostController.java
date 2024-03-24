package com.totm.totm.controller;

import com.totm.totm.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.totm.totm.dto.PostDto.AddPostRequestDto;
import static com.totm.totm.dto.PostDto.ModifyPostRequestDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @PostMapping("/add")
    public ResponseEntity addPost(@Valid @RequestBody AddPostRequestDto post) {
        postService.addPost(post);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts")
    public ResponseEntity findPosts(@RequestParam(required = false) Long lastId) {
        return ResponseEntity.ok(postService.findPosts(lastId));
    }

    @GetMapping("/my-post")
    public ResponseEntity findMyPosts(Long id, Pageable pageable) {
        return ResponseEntity.ok(postService.findMyPosts(id, pageable));
    }

    @GetMapping("/liked-posts")
    public ResponseEntity findLikedPosts(Long id, Pageable pageable) {
        return ResponseEntity.ok(postService.findLikedPosts(id, pageable));
    }

    @GetMapping("/reported")
    public ResponseEntity findReportedPosts(Pageable pageable) {
        return ResponseEntity.ok(postService.findReportedPosts(pageable));
    }

    @PatchMapping("/report/{id}")
    public ResponseEntity reportPost(@PathVariable Long id) {
        postService.reportPost(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/normalize/{id}")
    public ResponseEntity normalizePost(@PathVariable Long id) {
        postService.normalizePost(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/modify/{id}")
    public ResponseEntity modifyPost(@PathVariable Long id, @Valid @RequestBody ModifyPostRequestDto request) {
        postService.modifyPost(id, request);
        return ResponseEntity.ok().build();
    }
}
