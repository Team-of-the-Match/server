package com.totm.totm.repository;

import com.totm.totm.entity.Post;
import com.totm.totm.entity.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p left join fetch p.member m where p.postStatus = :postStatus order by p.lastModifiedDate asc")
    Page<Post> findReportedPosts(PostStatus postStatus, Pageable pageable);

    @Modifying
    @Query("update Post p set p.member = null where p.member.id = :id")
    void bulkPostMember(Long id);
}
