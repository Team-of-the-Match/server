package com.totm.totm.repository;

import com.totm.totm.entity.Likes;
import com.totm.totm.entity.Member;
import com.totm.totm.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    boolean existsLikesByMemberAndPost(Member member, Post post);

    Optional<Likes> findLikesByMemberAndPost(Member member, Post post);

    @Modifying
    @Query("update Likes l set l.member = null where l.member.id = :id")
    void bulkLikesMember(Long id);
}
