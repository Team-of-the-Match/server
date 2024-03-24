package com.totm.totm.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.totm.totm.entity.Comment;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.totm.totm.entity.QComment.comment1;

@RequiredArgsConstructor
public class QCommentRepositoryImpl implements QCommentRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> findCommentsByPostId(Long postId, Long lastCommentId) {
        return jpaQueryFactory
                .selectFrom(comment1)
                .where(ltCommentId(lastCommentId), comment1.post.id.eq(postId))
                .leftJoin(comment1.member).fetchJoin()
                .orderBy(comment1.id.desc())
                .limit(10)
                .fetch();
    }

    private BooleanExpression ltCommentId(Long lastCommentId) {
        if(lastCommentId == null) return null;
        return comment1.id.lt(lastCommentId);
    }
}
