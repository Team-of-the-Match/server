package com.totm.totm.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.totm.totm.entity.Post;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.totm.totm.entity.QPost.post;

@RequiredArgsConstructor
public class QPostRepositoryImpl implements QPostRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> findPosts(Long lastId) {
        return jpaQueryFactory
                .selectFrom(post)
                .where(ltPostId(lastId))
                .leftJoin(post.member).fetchJoin()
                .orderBy(post.id.desc())
                .limit(10)
                .fetch();
    }

    private BooleanExpression ltPostId(Long lastId) {
        if(lastId == null) return null;
        return post.id.lt(lastId);
    }
}
