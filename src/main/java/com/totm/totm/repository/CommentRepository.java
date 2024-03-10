package com.totm.totm.repository;

import com.totm.totm.entity.Comment;
import com.totm.totm.entity.CommentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long>, QCommentRepository {

//    @Query("select c from Comment c left join fetch c.member where c.post.id = :id order by c.createdDate desc")
//    Page<Comment> findCommentsByPost(Long id, Pageable pageable);

    @Query("select c from Comment c left join fetch c.member where c.commentStatus = :commentStatus order by c.lastModifiedDate asc")
    Page<Comment> findReportedComments(CommentStatus commentStatus, Pageable pageable);

    @Modifying
    @Query("update Comment c set c.member = null where c.member.id = :id")
    void bulkCommentMember(Long id);
}
