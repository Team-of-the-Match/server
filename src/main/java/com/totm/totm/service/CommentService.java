package com.totm.totm.service;

import com.totm.totm.entity.Comment;
import com.totm.totm.entity.CommentStatus;
import com.totm.totm.entity.Member;
import com.totm.totm.entity.Post;
import com.totm.totm.exception.CommentNotFoundException;
import com.totm.totm.exception.MemberNotFoundException;
import com.totm.totm.exception.MemberStopException;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        if(findMember.get().getStopDeadline() != null)
            if(LocalDateTime.now().isBefore(findMember.get().getStopDeadline()))
                throw new MemberStopException("현재 계정이 정지 상태입니다. 정지 일주일 후 정지가 해제됩니다.");
        Optional<Post> findPost = postRepository.findById(request.getPostId());
        if(findPost.isEmpty()) throw new PostNotFoundException("해당 게시글을 찾을 수 없음");

        Comment comment = new Comment(request.getComment(), CommentStatus.NORMAL);
        comment.setMemberAndPost(findMember.get(), findPost.get());
        findPost.get().increaseComment();
        commentRepository.save(comment);
    }

    public List<CommentResponseDto> findCommentsByPost(Long id, Long lastCommentId) {
        return commentRepository.findCommentsByPostId(id, lastCommentId)
                .stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void reportComment(Long id) {
        Optional<Comment> findComment = commentRepository.findById(id);
        if(findComment.isPresent()) {
            findComment.get().changeStatus(CommentStatus.REPORTED);
        } else throw new CommentNotFoundException("해당 댓글을 찾을 수 없음");
    }

    @Transactional
    public void normalizeComment(Long id) {
        Optional<Comment> findComment = commentRepository.findById(id);
        if(findComment.isPresent()) {
            findComment.get().changeStatus(CommentStatus.NORMAL);
        } else throw new CommentNotFoundException("해당 댓글을 찾을 수 없음");
    }

    public Page<ReportedCommentResponseDto> findReportedComments(Pageable pageable) {
        return commentRepository.findReportedComments(CommentStatus.REPORTED, pageable)
                .map(ReportedCommentResponseDto::new);
    }

    @Transactional
    public void deleteComment(DeleteCommentRequestDto request) {
        Optional<Post> findPost = postRepository.findById(request.getPostId());
        if(findPost.isEmpty()) throw new PostNotFoundException("해당 게시글을 찾을 수 없음");

        commentRepository.deleteById(request.getCommentId());
        findPost.get().decreaseComment();
    }
}
