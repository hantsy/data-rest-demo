package com.example.demo.web;

import com.example.demo.PostNotFoundException;
import com.example.demo.domain.Post;
import com.example.demo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class PostWebController {

    private final PostRepository postRepository;

    @GetMapping("")
    public String home() {
        return "redirect:/posts";
    }

    @GetMapping("/posts")
    public String listAllPosts(Model model) {
        List<Post> posts = this.postRepository.findAll();
        model.addAttribute("posts", posts);
        return "home";
    }


    @GetMapping("/posts/{id}")
    public String viewPostDetails(@PathVariable Long id,  Model model) {
        Post post = this.postRepository.findById(id).orElseThrow(()-> new PostNotFoundException(id));

        model.addAttribute("details", post);
        return "details";
    }
}
