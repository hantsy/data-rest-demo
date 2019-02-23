package com.example.demo.web;

import com.example.demo.PostNotFoundException;
import com.example.demo.domain.Post;
import com.example.demo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostWebController {

    private final PostRepository postRepository;

    @GetMapping("")
    public String home() {
        return "redirect:/posts";
    }

    @GetMapping("/posts")
    public String listAllPosts(ModelAndView mv) {
        List<Post> posts = this.postRepository.findAll();
        mv.addObject("posts", posts);
        return "post-list.xhtml";
    }


    @GetMapping("/posts/{id}")
    public String viewPostDetails(@PathVariable Long id,  ModelAndView mv) {
        Post post = this.postRepository.findById(id).orElseThrow(()-> new PostNotFoundException(id));

        mv.addObject("post", post);
        return "post-details.xhtml";
    }
}
