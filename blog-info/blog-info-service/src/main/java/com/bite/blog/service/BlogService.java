package com.bite.blog.service;

import com.bite.blog.api.pojo.AddBlogInfoRequest;
import com.bite.blog.api.pojo.BlogInfoResponse;
import com.bite.blog.api.pojo.UpBlogRequest;

import java.util.List;

public interface BlogService {
    List<BlogInfoResponse> getList();

    BlogInfoResponse getBlogDeatil(Integer blogId);

    Boolean addBlog(AddBlogInfoRequest addBlogInfoRequest);

    Boolean update(UpBlogRequest upBlogRequest);

    Boolean delete(Integer blogId);
}
