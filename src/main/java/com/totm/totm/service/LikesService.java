package com.totm.totm.service;

import com.totm.totm.entity.Likes;
import com.totm.totm.entity.Member;
import com.totm.totm.entity.Post;
import com.totm.totm.exception.AlreadyLikedException;
import com.totm.totm.exception.MemberNotFoundException;
import com.totm.totm.exception.NeverLikedException;
import com.totm.totm.exception.PostNotFoundException;
import com.totm.totm.repository.LikesRepository;
import com.totm.totm.repository.MemberRepository;
import com.totm.totm.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Optional;

import static com.totm.totm.dto.LikesDto.LikeRequestDto;

@Service
@Transactional(readOnly = true, rollbackFor = MethodArgumentNotValidException.class)
@RequiredArgsConstructor
public class LikesService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final LikesRepository likesRepository;

    @Transactional
    public int like(LikeRequestDto request) {
        Optional<Member> findMember = memberRepository.findById(request.getMemberId());
        if(findMember.isEmpty()) throw new MemberNotFoundException("해당 멤버를 찾을 수 없음");
        Optional<Post> findPost = postRepository.findById(request.getPostId());
        if(findPost.isEmpty()) throw new PostNotFoundException("해당 게시글을 찾을 수 없음");

        if(likesRepository.existsLikesByMemberAndPost(findMember.get(), findPost.get()))
            throw new AlreadyLikedException("이미 좋아한 글입니다.");

        Likes likes = new Likes(findMember.get(), findPost.get());
        likesRepository.save(likes);

        return findPost.get().increaseLike();
    }

    @Transactional
    public int dislike(LikeRequestDto request) {
        Optional<Member> findMember = memberRepository.findById(request.getMemberId());
        if(findMember.isEmpty()) throw new MemberNotFoundException("해당 멤버를 찾을 수 없음");
        Optional<Post> findPost = postRepository.findById(request.getPostId());
        if(findPost.isEmpty()) throw new PostNotFoundException("해당 게시글을 찾을 수 없음");

        Optional<Likes> findLikes = likesRepository.findLikesByMemberAndPost(findMember.get(), findPost.get());
        if(findLikes.isPresent()) {
            likesRepository.delete(findLikes.get());
        } else throw new NeverLikedException("이 글을 좋아한 적 없습니다.");

        return findPost.get().decreaseLike();
    }
}
