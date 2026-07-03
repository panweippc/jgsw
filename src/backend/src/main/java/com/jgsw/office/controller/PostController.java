package com.jgsw.office.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jgsw.office.entity.SysPost;
import com.jgsw.office.service.PostService;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public Result<IPage<SysPost>> getPostList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String postName) {
        return postService.getPostList(page, size, postName);
    }

    @GetMapping("/{id}")
    public Result<SysPost> getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @PostMapping
    public Result<SysPost> createPost(@RequestBody SysPost post) {
        return postService.createPost(post);
    }

    @PutMapping("/{id}")
    public Result<SysPost> updatePost(@PathVariable Long id, @RequestBody SysPost post) {
        post.setId(id);
        return postService.updatePost(post);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deletePost(@PathVariable Long id) {
        return postService.deletePost(id);
    }

    @GetMapping("/all")
    public Result<List<SysPost>> getAllPosts() {
        return postService.getAllPosts();
    }
}
