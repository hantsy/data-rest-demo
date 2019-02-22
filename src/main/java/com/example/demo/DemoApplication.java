package com.example.demo;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.http.ResponseEntity.*;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}


@Component
@Slf4j
@RequiredArgsConstructor
class DataIntializer implements CommandLineRunner {

    private final PostRepository posts;

    @Override
    public void run(String... args) throws Exception {
        log.debug("data initialization ... ");

        List<Post> postData = IntStream.range(1, 3)
            .mapToObj(
                num -> Post.builder().title("title#" + num).content("content#" + num).build()
            ).collect(Collectors.toList());

        this.posts.saveAll(postData);

        for (Post post : this.posts.findAll()) {
            log.debug("saved post: {}", post);
        }
    }
}


@RepositoryRestController
@RequestMapping("/posts")
@RequiredArgsConstructor
class CustomRepositoryRestController {
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


@BasePathAwareController
@RequestMapping("/posts")
@RequiredArgsConstructor
class PostController {
    private final PostRepository posts;
    private final PostService service;

    @PutMapping("/{id}/status")
    ResponseEntity save(@PathVariable Long id, @RequestBody PostStatusRequest statusRequest) {
        this.posts.findById(id)
            .map(
                p -> {
                    p.setStatus(Status.valueOf(statusRequest.getStatus().toUpperCase()));
                    return this.service.savePost(p);
                }
            )
            .orElseThrow(() -> new PostNotFoundException(id));
        return noContent().build();
    }

}

@RestControllerAdvice
@Slf4j
class PostExceptionHandler {

    @ExceptionHandler()
    public ResponseEntity vehicleNotFound(PostNotFoundException ex, WebRequest request) {
        log.debug("handling PostNotFoundException:{}" + ex.getMessage());
        return notFound().build();
    }
}


@Data
class PostStatusRequest {
    private String status;
}

@Service
@Slf4j
@RequiredArgsConstructor
class PostService {

    private final PostRepository postRepository;

    public Post savePost(Post post) {
        log.debug("PostService::saving post: {}", post);
        return this.postRepository.save(post);
    }
}

@RepositoryRestResource(collectionResourceRel = "posts", itemResourceRel = "post")
interface PostRepository extends JpaRepository<Post, Long> {

    @RestResource(exported = false)
    @Override
    <S extends Post> S save(S s);

//    @RestResource(exported = false)
//    @Override
//    <S extends Post> S saveAndFlush(S s);
//
//    @Override
//    @RestResource(exported = false)
//    <S extends Post> List<S> saveAll(Iterable<S> iterable);
}

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String content;

    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private Status status = Status.DRAFT;

}

enum Status {
    DRAFT,
    PUBLISHED
}

class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(Long id) {
        super(String.format("post: {} not found", id));
    }
}



