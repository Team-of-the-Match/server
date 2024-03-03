package com.totm.totm.service;

import com.totm.totm.repository.PostRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class LikesServiceTest {
    @Autowired
    PostRepository postRepository;
    @Autowired
    EntityManager em;
}