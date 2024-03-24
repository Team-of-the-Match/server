package com.totm.totm.repository;

import com.totm.totm.entity.Comment;
import com.totm.totm.entity.CommentStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, QCommentRepository {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Comment> findById(Long id);

    @Query("select c from Comment c left join fetch c.member where c.commentStatus = :commentStatus order by c.lastModifiedDate asc")
    Page<Comment> findReportedComments(CommentStatus commentStatus, Pageable pageable);

    @Modifying
    @Query("update Comment c set c.member = null where c.member.id = :id")
    void bulkCommentMember(Long id);
}
