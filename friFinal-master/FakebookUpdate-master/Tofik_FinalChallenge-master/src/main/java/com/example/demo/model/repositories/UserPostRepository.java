package com.example.demo.model.repositories;

import com.example.demo.model.Profile;
import com.example.demo.model.UserPost;
import org.springframework.data.repository.CrudRepository;

public interface UserPostRepository extends CrudRepository<UserPost, Long> {
    Iterable<UserPost> findAllByFollowedTrueOrderByTimePostedDesc();
    Iterable<UserPost> findAllByTextContaining(String searchTerm);
    Iterable<UserPost> findAllByWhoPosted(Profile profile);
}