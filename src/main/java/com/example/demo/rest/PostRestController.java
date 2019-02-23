package com.example.demo.rest;


import com.example.demo.PostNotFoundException;
import com.example.demo.domain.Post;
import com.example.demo.domain.Status;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.ResponseEntity.noContent;

@BasePathAwareController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostRestController {
    private final PostRepository posts;
    private final PostService service;

    @PutMapping("/{id}/status")
    ResponseEntity save(@PathVariable Long id, @RequestBody PostStatusRequest statusRequest) {
        Post post = this.posts.findById(id).orElseThrow(() -> new PostNotFoundException(id));
        this.service.savePost(post);

        return noContent().build();
    }

}
