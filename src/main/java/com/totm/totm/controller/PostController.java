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

    @GetMapping("/reported")
    public ResponseEntity findReportedPosts(Pageable pageable) {
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                        .body(NormalResponse.builder().status(200).data(postService.findReportedPosts(pageable)).build());
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(NormalResponse.builder().status(200).data("success").build());
    }

    @PostMapping("/add")
    public ResponseEntity addPost(@Valid @RequestBody AddPostRequestDto post) {
        postService.addPost(post);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(NormalResponse.builder().status(200).data("success").build());
    }

}
