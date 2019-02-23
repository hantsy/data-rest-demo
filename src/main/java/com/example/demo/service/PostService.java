package com.example.demo.service;

import com.example.demo.domain.Post;
import com.example.demo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post savePost(Post post) {
        log.debug("PostService::saving post: {}", post);
        return this.postRepository.save(post);
    }
}
