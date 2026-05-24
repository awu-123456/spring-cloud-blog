package com.bite.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bite.blog.dataobject.BlogInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogMapper extends BaseMapper<BlogInfo> {
}
