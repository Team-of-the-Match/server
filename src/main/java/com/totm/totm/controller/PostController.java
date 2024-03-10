package com.totm.totm.controller;

import com.totm.totm.dto.NormalResponse;
import com.totm.totm.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.totm.totm.dto.PostDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @PostMapping("/add")
    public ResponseEntity addPost(@Valid @RequestBody AddPostRequestDto post) {
        postService.addPost(post);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(NormalResponse.builder().status(200).build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(NormalResponse.builder().status(200).build());
    }

    @GetMapping("/posts")
    public ResponseEntity findPosts(@RequestParam(required = false) Long lastId) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(postService.findPosts(lastId)).build());
    }

    @GetMapping("/my-post")
    public ResponseEntity findMyPosts(Long id, Pageable pageable) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(postService.findMyPosts(id, pageable)).build());
    }

    @GetMapping("/liked-posts")
    public ResponseEntity findLikedPosts(Long id, Pageable pageable) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(NormalResponse.builder().status(200).data(postService.findLikedPosts(id, pageable)).build());
    }

    @GetMapping("/reported")
    public ResponseEntity findReportedPosts(Pageable pageable) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                        .body(NormalResponse.builder().status(200).data(postService.findReportedPosts(pageable)).build());
    }

    @PatchMapping("/report/{id}")
    public ResponseEntity reportPost(@PathVariable Long id) {
        postService.reportPost(id);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(NormalResponse.builder().status(200).build());
    }

    @PatchMapping("/normalize/{id}")
    public ResponseEntity normalizePost(@PathVariable Long id) {
        postService.normalizePost(id);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(NormalResponse.builder().status(200).build());
    }

    @PatchMapping("/modify/{id}")
    public ResponseEntity modifyPost(@PathVariable Long id, @Valid @RequestBody ModifyPostRequestDto request) {
        postService.modifyPost(id, request);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(NormalResponse.builder().status(200).build());
    }
}
