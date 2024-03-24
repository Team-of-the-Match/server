package com.totm.totm.service;

import com.totm.totm.entity.Notification;
import com.totm.totm.exception.NotificationNotFoundException;
import com.totm.totm.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.totm.totm.dto.NotificationDto.*;


@Service
@Transactional(readOnly = true, rollbackFor = MethodArgumentNotValidException.class)
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void post(AddNotificationRequestDto request) {
        notificationRepository.save(new Notification(request.getTitle(), request.getContent()));
    }

    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }

    public List<NotificationsResponseDto> findNotifications(Long lastId) {
        return notificationRepository.findNotifications(lastId)
                .stream()
                .map(NotificationsResponseDto::new)
                .collect(Collectors.toList());
    }

    public NotificationResponseDto getNotification(Long id) {
        Optional<Notification> result = notificationRepository.findById(id);
        if(result.isPresent()) {
            return new NotificationResponseDto(result.get());
        } else throw new NotificationNotFoundException("해당 공지를 찾을 수 없습니다.");
    }

    @Transactional
    public void modifyNotification(Long id, ModifyNotificationRequestDto request) {
        Optional<Notification> findNotification = notificationRepository.findById(id);
        if(findNotification.isPresent()) {
            findNotification.get().modifyNotification(request.getTitle(), request.getContent());
        } else throw new NotificationNotFoundException("해당 공지를 찾을 수 없습니다.");
    }
}
