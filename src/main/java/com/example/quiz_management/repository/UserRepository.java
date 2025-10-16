package com.example.quiz_management.repository;


import com.example.quiz_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

//        Optional<User> findB(String username);

        User findByEmail(String email);

//        boolean existsByUsername(String username);

        boolean existsByEmail(String email);

        @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.name = :roleName")
        long countByRoleName(@Param("roleName") String roleName);

}
