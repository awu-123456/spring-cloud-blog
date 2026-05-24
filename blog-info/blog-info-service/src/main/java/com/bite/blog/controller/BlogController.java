package com.bite.blog.controller;

import com.bite.blog.api.BlogServiceAPI;
import com.bite.blog.api.pojo.AddBlogInfoRequest;
import com.bite.blog.api.pojo.BlogInfoResponse;
import com.bite.blog.api.pojo.UpBlogRequest;
import com.bite.blog.service.BlogService;
import com.bite.common.pojo.Result;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/blog")
@RestController
public class BlogController implements BlogServiceAPI {
    @Autowired
    private BlogService blogService;

    @Override
    public Result<List<BlogInfoResponse>> getList(){
        return Result.success(blogService.getList());
    }

    @Override
    public Result<BlogInfoResponse> getBlogDeatail(@NotNull @RequestParam("blogId") Integer blogId){
        log.info("getBlogDetail, blogId: {}", blogId);
        return Result.success(blogService.getBlogDeatil(blogId));
    }

    @Override
    public Result<Boolean> addBlog(@Validated @RequestBody AddBlogInfoRequest addBlogInfoRequest){
        log.info("addBlog 接收参数: "+ addBlogInfoRequest);
        return Result.success(blogService.addBlog(addBlogInfoRequest));
    }

    @Override
    public Result<Boolean> updateBlog(@Valid @RequestBody UpBlogRequest upBlogRequest){
        log.info("updateBlog 接收参数: "+ upBlogRequest);
        return Result.success(blogService.update(upBlogRequest));

    }

    @Override
    public Result<Boolean> deleteBlog(@NotNull @RequestParam("blogId") Integer blogId){
        log.info("deleteBlog 接收参数: "+ blogId);
        return Result.success(blogService.delete(blogId));
    }
}
