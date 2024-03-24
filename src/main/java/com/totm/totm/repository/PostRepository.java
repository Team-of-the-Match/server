package com.totm.totm.repository;

import com.totm.totm.entity.Likes;
import com.totm.totm.entity.Post;
import com.totm.totm.entity.PostStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, QPostRepository {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Post> findById(Long id);

//    @Lock(LockModeType.OPTIMISTIC)
//    @Query("select p from Post p where p.id = :id")
//    Optional<Post> findByIdWithOpt(Long id);
//
//    @Query("select p from Post p where p.id = :id")
//    Optional<Post> findByIdWithoutLock(Long id);

    @Query("select p from Post p left join fetch p.member m where m.id = :id order by p.createdDate desc")
    Page<Post> findPostsByMemberId(Long id, Pageable pageable);

    @Query("select l from Likes l " +
            "join fetch l.post p " +
            "left join fetch p.member m where m.id = :id order by p.createdDate desc")
    Page<Likes> findLikedPostsByMemberId(Long id, Pageable pageable);

    @Query("select p from Post p left join fetch p.member m where p.postStatus = :postStatus order by p.lastModifiedDate asc")
    Page<Post> findReportedPosts(PostStatus postStatus, Pageable pageable);

    @Modifying
    @Query("update Post p set p.member = null where p.member.id = :id")
    void bulkPostMember(Long id);
}
