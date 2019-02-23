package com.example.demo.repository;

import com.example.demo.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "posts", itemResourceRel = "post")
public interface PostRepository extends JpaRepository<Post, Long> {

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
