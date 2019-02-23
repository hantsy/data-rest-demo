package com.example.demo;

import com.example.demo.domain.Post;
import com.example.demo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataIntializer implements CommandLineRunner {

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
