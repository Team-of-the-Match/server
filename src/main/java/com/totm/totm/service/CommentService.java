package com.totm.totm.service;

import com.totm.totm.entity.CommentStatus;
import com.totm.totm.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static com.totm.totm.dto.CommentDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, rollbackFor = MethodArgumentNotValidException.class)
public class CommentService {

    private final CommentRepository commentRepository;

    public Page<ReportedCommentResponseDto> findReportedComments(Pageable pageable) {
        return commentRepository.findReportedComments(CommentStatus.REPORTED, pageable)
                .map(ReportedCommentResponseDto::new);
    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
