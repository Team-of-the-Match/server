package com.totm.totm.repository;

import com.totm.totm.entity.Comment;
import com.totm.totm.entity.CommentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c left join fetch c.member where c.commentStatus = :commentStatus order by c.lastModifiedDate asc")
    Page<Comment> findReportedComments(CommentStatus commentStatus, Pageable pageable);
}
