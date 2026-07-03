package com.jgsw.office.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgsw.office.entity.SysPost;
import com.jgsw.office.mapper.SysPostMapper;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private SysPostMapper sysPostMapper;

    public Result<IPage<SysPost>> getPostList(int page, int size, String postName) {
        Page<SysPost> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<SysPost> queryWrapper = new LambdaQueryWrapper<>();
        if (postName != null && !postName.isEmpty()) {
            queryWrapper.like(SysPost::getPostName, postName);
        }
        queryWrapper.eq(SysPost::getStatus, 1);
        queryWrapper.orderByAsc(SysPost::getSortOrder);
        IPage<SysPost> postPage = sysPostMapper.selectPage(pageParam, queryWrapper);
        return Result.success(postPage);
    }

    public Result<SysPost> getPostById(Long id) {
        SysPost post = sysPostMapper.selectById(id);
        if (post == null) {
            return Result.error(404, "岗位不存在");
        }
        return Result.success(post);
    }

    @Transactional
    public Result<SysPost> createPost(SysPost post) {
        LambdaQueryWrapper<SysPost> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysPost::getPostCode, post.getPostCode());
        if (sysPostMapper.selectOne(queryWrapper) != null) {
            return Result.error(400, "岗位编码已存在");
        }

        post.setStatus(1);
        sysPostMapper.insert(post);
        return Result.success("创建成功", post);
    }

    @Transactional
    public Result<SysPost> updatePost(SysPost post) {
        SysPost existing = sysPostMapper.selectById(post.getId());
        if (existing == null) {
            return Result.error(404, "岗位不存在");
        }

        sysPostMapper.updateById(post);
        return Result.success("更新成功", post);
    }

    @Transactional
    public Result<Void> deletePost(Long id) {
        SysPost post = sysPostMapper.selectById(id);
        if (post == null) {
            return Result.error(404, "岗位不存在");
        }

        post.setStatus(0);
        sysPostMapper.updateById(post);
        return Result.success("删除成功");
    }

    public Result<List<SysPost>> getAllPosts() {
        List<SysPost> posts = sysPostMapper.selectList(new LambdaQueryWrapper<SysPost>().eq(SysPost::getStatus, 1));
        return Result.success(posts);
    }
}
