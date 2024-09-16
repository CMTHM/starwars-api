package com.example.starwars.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.starwars.entity.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findByName(String username);

}