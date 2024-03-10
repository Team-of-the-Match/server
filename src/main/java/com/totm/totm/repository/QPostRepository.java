package com.totm.totm.repository;

import com.totm.totm.entity.Post;

import java.util.List;

public interface QPostRepository {

    List<Post> findPosts(Long lastId);
}
