package com.example.demo.rest;

import com.example.demo.domain.Post;
import com.example.demo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.http.ResponseEntity.created;

@RepositoryRestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class CustomPostRepositoryRestController {
    private final PostRepository postRepository;

    @PostMapping()
    ResponseEntity save(@RequestBody Post post, ServletUriComponentsBuilder uriComponentsBuilder) {
        Post saved = this.postRepository.save(post);

        return created(
            uriComponentsBuilder
                .path("/api/posts/{id}")
                .buildAndExpand(saved.getId())
                .toUri())
            .build();
    }
}
