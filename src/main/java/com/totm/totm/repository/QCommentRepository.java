package com.totm.totm.repository;

import com.totm.totm.entity.Comment;

import java.util.List;

public interface QCommentRepository {

    List<Comment> findCommentsByPostId(Long postId, Long lastCommentId);
}
