package com.totm.totm.repository;

import com.totm.totm.entity.Manager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    Optional<Manager> findByUsername(String username);

    Page<Manager> findManagersByNameContainsOrderByCreatedDateDesc(String name, Pageable pageable);
}
