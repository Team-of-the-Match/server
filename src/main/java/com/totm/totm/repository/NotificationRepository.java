package com.totm.totm.repository;

import com.totm.totm.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long>, QNotificationRepository {

}
