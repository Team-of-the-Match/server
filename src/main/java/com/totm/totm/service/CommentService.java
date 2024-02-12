package com.totm.totm.service;

import com.totm.totm.entity.Comment;
import com.totm.totm.entity.CommentStatus;
import com.totm.totm.entity.Member;
import com.totm.totm.entity.Post;
import com.totm.totm.exception.CommentNotFoundException;
import com.totm.totm.exception.MemberNotFoundException;
import com.totm.totm.exception.PostNotFoundException;
import com.totm.totm.repository.CommentRepository;
import com.totm.totm.repository.MemberRepository;
import com.totm.totm.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Optional;

import static com.totm.totm.dto.CommentDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, rollbackFor = MethodArgumentNotValidException.class)
public class CommentService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void addComment(AddCommentRequestDto request) {
        Optional<Member> findMember = memberRepository.findById(request.getMemberId());
        if(findMember.isEmpty()) throw new MemberNotFoundException("해당 멤버를 찾을 수 없음");
        Optional<Post> findPost = postRepository.findById(request.getPostId());
        if(findPost.isEmpty()) throw new PostNotFoundException("해당 게시글을 찾을 수 없음");

        Comment comment = new Comment(request.getComment(), CommentStatus.NORMAL);
        comment.setMemberAndPost(findMember.get(), findPost.get());
        findPost.get().increaseComment();
        commentRepository.save(comment);
    }

    public Page<CommentResponseDto> findCommentsByPost(Long id, Pageable pageable) {
        return commentRepository.findCommentsByPost(id, pageable)
                .map(CommentResponseDto::new);
    }

    @Transactional
    public void reportComment(Long id) {
        Optional<Comment> findComment = commentRepository.findById(id);
        if(findComment.isPresent()) {
            findComment.get().reportComment();
        } else throw new CommentNotFoundException("해당 댓글을 찾을 수 없음");
    }

    public void reportPost(Long id) {
    }

    public Page<ReportedCommentResponseDto> findReportedComments(Pageable pageable) {
        return commentRepository.findReportedComments(CommentStatus.REPORTED, pageable)
                .map(ReportedCommentResponseDto::new);
    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
