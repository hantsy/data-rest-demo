package com.example.demo;


import com.example.demo.domain.Post;
import com.example.demo.domain.Status;
import com.example.demo.repository.PostRepository;
import com.example.demo.rest.PostStatusRequest;
import com.example.demo.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
public class DemoApplicationTests {

    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext applicationContext;

    @Autowired
    ObjectMapper objectMapper;


    @MockBean
    PostRepository posts;

    @MockBean
    PostService postService;

    @BeforeEach
    public void setup() {
        this.mockMvc = webAppContextSetup(this.applicationContext)
            .apply(springSecurity())
            .build();
    }

    @AfterEach
    public void tearDown() {
        //Mockito.reset(this.posts, this.postService);
    }


    @Test
    @WithMockUser
    public void testUpdateStatus() throws Exception {
        given(this.posts.findById(anyLong()))
            .willReturn(Optional.of(
                Post.builder()
                    .id(1L)
                    .title("test title")
                    .content("test content")
                    .build())
            );

        given(this.postService.savePost(any(Post.class)))
            .willReturn(
                Post.builder()
                    .id(1L)
                    .title("test title")
                    .content("test content")
                    .status(Status.PUBLISHED)
                    .build()
            );


        this.mockMvc
            .perform(
                put("/api/posts/1/status")
                    .content(this.objectMapper.writeValueAsBytes(PostStatusRequest.builder().status("PUBLISHED").build()))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNoContent());

        verify(this.posts, times(1)).findById(any(Long.class));
        verify(this.postService, times(1)).savePost(any(Post.class));
        verifyNoMoreInteractions(this.posts);
        verifyNoMoreInteractions(this.postService);
    }

    @Test
    @WithMockUser
    public void testUpdatePostStatus_WhenPostNotFound_shouldThrowException() throws Exception {
        given(this.posts.findById(anyLong()))
            .willReturn(Optional.empty());

        given(this.postService.savePost(any(Post.class)))
            .willReturn(
                Post.builder()
                    .id(1L)
                    .title("test title")
                    .content("test content")
                    .status(Status.PUBLISHED)
                    .build()
            );


        this.mockMvc
            .perform(
                put("/api/posts/2/status")
                    .content(this.objectMapper.writeValueAsBytes(PostStatusRequest.builder().status("PUBLISHED").build()))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNotFound());


        verify(this.posts, times(1)).findById(anyLong());
        //verify(this.postService, times(1)).savePost(any(Post.class));
        verifyNoMoreInteractions(this.posts);
        //verifyNoMoreInteractions(this.postService);
    }
}
