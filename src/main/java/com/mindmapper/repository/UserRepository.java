package com.mindmapper.repository;

import com.mindmapper.entity.UserInfo;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long> {

    // Custom query methods can be defined here if needed
    // For example, to find a user by email:
     Optional<UserInfo> findByEmail(String email);
}
