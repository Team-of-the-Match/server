package com.totm.totm.repository;

import com.totm.totm.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select n from Notification n order by n.createdDate desc")
    Page<Notification> findNotifications(Pageable pageable);
}
