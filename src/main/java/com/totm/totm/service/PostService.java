package com.totm.totm.service;

import com.totm.totm.entity.Member;
import com.totm.totm.entity.Post;
import com.totm.totm.entity.PostStatus;
import com.totm.totm.exception.MemberNotFoundException;
import com.totm.totm.exception.MemberStopException;
import com.totm.totm.exception.PostNotFoundException;
import com.totm.totm.repository.MemberRepository;
import com.totm.totm.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.totm.totm.dto.PostDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, rollbackFor = MethodArgumentNotValidException.class)
public class PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public void addPost(AddPostRequestDto request) {
        Post post = new Post(request.getTitle(), request.getContent(), PostStatus.NORMAL, 0, 0);
        Optional<Member> findMember = memberRepository.findById(request.getMemberId());
        if(findMember.isPresent()) {
            if(findMember.get().getStopDeadline() == null || LocalDateTime.now().isAfter(findMember.get().getStopDeadline())) {
                post.setMember(findMember.get());
                postRepository.save(post);
            } else throw new MemberStopException("현재 계정이 정지 상태입니다. 정지 일주일 후 정지가 해제됩니다.");
        } else throw new MemberNotFoundException("해당 멤버를 찾을 수 없습니다.");
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public Page<PostsResponseDto> findPosts(Pageable pageable) {
        return postRepository.findPosts(pageable)
                .map(PostsResponseDto::new);
    }

    public Page<PostsResponseDto> findMyPosts(Long id, Pageable pageable) {
        return postRepository.findPostsByMemberId(id, pageable)
                .map(PostsResponseDto::new);
    }

    public Page<PostsResponseDto> findLikedPosts(Long id, Pageable pageable) {
        return postRepository.findLikedPostsByMemberId(id, pageable)
                .map(PostsResponseDto::new);
    }

    public Page<ReportedPostResponseDto> findReportedPosts(Pageable pageable) {
        return postRepository.findReportedPosts(PostStatus.REPORTED, pageable)
                .map(ReportedPostResponseDto::new);
    }

    @Transactional
    public void reportPost(Long id) {
        Optional<Post> findPost = postRepository.findById(id);
        if(findPost.isPresent()) {
            findPost.get().reportPost();
        } else throw new PostNotFoundException("해당 게시글을 찾을 수 없습니다.");
    }

    @Transactional
    public void modifyPost(Long id, ModifyPostRequestDto request) {
        Optional<Post> findPost = postRepository.findById(id);
        if(findPost.isPresent()) {
            findPost.get().modifyPost(request.getTitle(), request.getContent());
        } else throw new PostNotFoundException("해당 게시글을 찾을 수 없습니다.");
    }
}
