package com.example.smartqueue.repository;

import com.example.smartqueue.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}