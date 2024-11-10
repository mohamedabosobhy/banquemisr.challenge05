package com.task.management.repository;

import com.task.management.entity.History;
import com.task.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, Long> {

}
