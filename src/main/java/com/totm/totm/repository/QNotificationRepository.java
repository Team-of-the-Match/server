package com.totm.totm.repository;

import com.totm.totm.entity.Notification;

import java.util.List;

public interface QNotificationRepository {

    List<Notification> findNotifications(Long lastId);
}
