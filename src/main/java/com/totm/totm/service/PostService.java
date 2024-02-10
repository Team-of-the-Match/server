package com.totm.totm.service;

import com.totm.totm.entity.Member;
import com.totm.totm.entity.Post;
import com.totm.totm.entity.PostStatus;
import com.totm.totm.exception.MemberNotFoundException;
import com.totm.totm.repository.MemberRepository;
import com.totm.totm.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Optional;

import static com.totm.totm.dto.PostDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, rollbackFor = MethodArgumentNotValidException.class)
public class PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public Page<ReportedPostResponseDto> findReportedPosts(Pageable pageable) {
        return postRepository.findReportedPosts(PostStatus.REPORTED, pageable)
                .map(ReportedPostResponseDto::new);
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional
    public void addPost(AddPostRequestDto request) {
        Post post = new Post(request.getTitle(), request.getContent(), PostStatus.NORMAL);
        Optional<Member> findMember = memberRepository.findById(request.getMemberId());
        if(findMember.isPresent()) {
            post.setMember(findMember.get());
            findMember.get().getPosts().add(post);
            postRepository.save(post);
        } else throw new MemberNotFoundException("해당 멤버를 찾을 수 없습니다.");
    }
}
