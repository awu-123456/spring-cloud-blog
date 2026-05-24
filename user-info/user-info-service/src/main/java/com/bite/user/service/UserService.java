package com.bite.user.service;

import com.bite.user.api.pojo.UserInfoRegisterRequest;
import com.bite.user.api.pojo.UserInfoRequest;
import com.bite.user.api.pojo.UserInfoResponse;
import com.bite.user.api.pojo.UserLoginResponse;

public interface UserService {
    UserLoginResponse login(UserInfoRequest user);

    UserInfoResponse getUserInfo(Integer userId);

    UserInfoResponse selectAuthorInfoByBlogId(Integer blogId);

    Integer register(UserInfoRegisterRequest userInfoRegisterRequest);
}
