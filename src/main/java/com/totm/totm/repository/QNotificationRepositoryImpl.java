package com.totm.totm.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.totm.totm.entity.Notification;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.totm.totm.entity.QNotification.notification;

@RequiredArgsConstructor
public class QNotificationRepositoryImpl implements QNotificationRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Notification> findNotifications(Long lastId) {
        return jpaQueryFactory
                .selectFrom(notification)
                .where(ltNotificationId(lastId))
                .orderBy(notification.id.desc())
                .limit(10)
                .fetch();
    }

    private BooleanExpression ltNotificationId(Long lastId) {
        if(lastId == null) return null;
        return notification.id.lt(lastId);
    }
}
