package com.df.fne.jpa.repositories;

import com.df.fne.jpa.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
