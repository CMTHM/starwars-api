package com.example.starwars.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.starwars.entity.UserInfo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.hibernate.query.sqm.FetchClauseType;

@DataJpaTest
public class StarWarsRepositoyTests {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @BeforeEach
    void setUp() {
        // Optionally initialize data before each test
    	userInfoRepository.deleteAll();
    }

    @Test
    void testSaveUser() {
        UserInfo user = new UserInfo();
        user.setName("Luke Skywalker");
        user.setEmail("chin@gmal.com");
        user.setPassword("c@123");
        user.setRoles("ADMIN");

        UserInfo savedPerson = userInfoRepository.save(user);

        assertThat(savedPerson).isNotNull();
        assertThat(savedPerson.getId()).isNotNull();
        assertThat(savedPerson.getName()).isEqualTo("Luke Skywalker");
        assertThat(savedPerson.getRoles()).isEqualTo("ADMIN");
    }

    @Test
    void testFindByName() {
    	UserInfo user = new UserInfo();
        user.setName("Luke Skywalker");
        user.setEmail("chin@gmal.com");
        user.setPassword("c@123");
        user.setRoles("ADMIN");
        userInfoRepository.save(user);

        Optional<UserInfo> userInfo = userInfoRepository.findByName("Luke Skywalker");

        assertThat(userInfo).isNotNull();
        assertThat(user.getName()).isEqualTo("Luke Skywalker");
    }

  
}

