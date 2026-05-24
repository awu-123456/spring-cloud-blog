package com.bite.user.convert;

import com.bite.common.utils.SecurityUtil;
import com.bite.user.api.pojo.UserInfoRegisterRequest;
import com.bite.user.dataobject.UserInfo;

public class BeanConvert {
    public static UserInfo convertUserInfoByEncrypt(UserInfoRegisterRequest userInfoRegisterRequest) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(userInfoRegisterRequest.getUserName());
        userInfo.setPassword(SecurityUtil.encrypt(userInfoRegisterRequest.getPassword()));
        userInfo.setGithubUrl(userInfoRegisterRequest.getGithubUrl());
        userInfo.setEmail(userInfoRegisterRequest.getEmail());
        return userInfo;
    }
}
