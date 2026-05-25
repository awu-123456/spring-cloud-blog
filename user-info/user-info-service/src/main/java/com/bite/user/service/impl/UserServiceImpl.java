package com.bite.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bite.blog.api.BlogServiceAPI;
import com.bite.blog.api.pojo.BlogInfoResponse;
import com.bite.common.exception.BlogException;
import com.bite.common.pojo.Result;
import com.bite.common.utils.*;
import com.bite.user.api.pojo.UserInfoRegisterRequest;
import com.bite.user.api.pojo.UserInfoRequest;
import com.bite.user.api.pojo.UserInfoResponse;
import com.bite.user.api.pojo.UserLoginResponse;
import com.bite.user.convert.BeanConvert;
import com.bite.user.dataobject.UserInfo;
import com.bite.user.mapper.UserInfoMapper;
import com.bite.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private static final long EXPIRE_TIME = 14 * 24 * 60 * 60;
    private static final String USER_PREFIX = "user";

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private BlogServiceAPI blogServiceAPI;
    @Autowired
    private Redis redis;

    @Override
    public UserLoginResponse login(UserInfoRequest user) {
        //验证账号密码是否正确
        UserInfo userInfo = queryUserInfo(user.getUserName());
        if (userInfo==null || userInfo.getId()==null){
            throw new BlogException("用户不存在");
        }
//        if (!user.getPassword().equals(userInfo.getPassword())){
//            throw new BlogException("用户密码不正确");
//        }
        if (!SecurityUtil.verify(user.getPassword(),userInfo.getPassword())){
            throw new BlogException("用户密码不正确");
        }
        //账号密码正确的逻辑
        Map<String,Object> claims = new HashMap<>();
        claims.put("id", userInfo.getId());
        claims.put("name", userInfo.getUserName());

        String jwt = JWTUtils.genJwt(claims);
        return new UserLoginResponse(userInfo.getId(), jwt);
    }

    private UserInfo queryUserInfo(String userName) {
        String key = buildKey(userName);
        boolean exists = redis.hasKey(key);
        if(exists) {
            log.info("从redis中获取数据, key:{}", key);
            String userJson = redis.get(key);
            UserInfo userInfo = JsonUtil.parseJson(userJson, UserInfo.class);
            return userInfo == null ? null : userInfo;
        } else {
            log.info("从mysql中获取数据, userName:{}", userName);
            UserInfo userInfo = selectUserInfoByName(userName);
            redis.set(key, JsonUtil.toJson(userInfo), EXPIRE_TIME);
            return userInfo;
        }
    }

    @Override
    public UserInfoResponse getUserInfo(Integer userId) {
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        UserInfo userInfo = selectUserInfoById(userId);
        BeanUtils.copyProperties(userInfo, userInfoResponse);
        return userInfoResponse;
    }

    @Override
    public UserInfoResponse selectAuthorInfoByBlogId(Integer blogId) {
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        //1. 根据博客ID, 获取作者ID
        Result<BlogInfoResponse> blogDeatail = blogServiceAPI.getBlogDeatail(blogId);
        //2. 根据作者ID, 获取作者信息
        if (blogDeatail == null ||  blogDeatail.getData() == null) {
            throw new BlogException("博客不存在");
        }
        UserInfo userInfo = selectUserInfoById(blogDeatail.getData().getUserId());
        BeanUtils.copyProperties(userInfo, userInfoResponse);
        return userInfoResponse;
    }

    @Override
    public Integer register(UserInfoRegisterRequest userInfoRegisterRequest) {
        checkUserInfo(userInfoRegisterRequest);
        UserInfo userInfo = BeanConvert.convertUserInfoByEncrypt(userInfoRegisterRequest);
        try {
            int result = userInfoMapper.insert(userInfo);
            if(result == 1) {
                redis.set(buildKey(userInfo.getUserName()), JsonUtil.toJson(userInfo), EXPIRE_TIME);
                return userInfo.getId();
            } else {
                throw new BlogException("用户注册失败");
            }
        } catch (Exception e) {
            log.error("用户注册失败,e:",e);
            throw new BlogException("用户注册失败");
        }
    }

    private String buildKey(String userName) {
        return redis.buildKey(USER_PREFIX, userName);
    }

    private void checkUserInfo(UserInfoRegisterRequest userInfoRegisterRequest) {
        UserInfo userInfo = selectUserInfoByName(userInfoRegisterRequest.getUserName());
        if(userInfo != null) {
            throw new BlogException("用户名已经存在");
        }

        if (!RegexUtil.checkMail(userInfoRegisterRequest.getEmail())) {
            throw new BlogException("邮箱格式不合法");
        }

        if(!RegexUtil.checkURL(userInfoRegisterRequest.getGithubUrl())) {
            throw new BlogException("githubUrl格式不合法");
        }
    }

    public UserInfo selectUserInfoByName(String userName) {
        return userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getUserName, userName).eq(UserInfo::getDeleteFlag, 0));
    }
    private UserInfo selectUserInfoById(Integer userId) {
        return userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getId, userId).eq(UserInfo::getDeleteFlag, 0));
    }
}
