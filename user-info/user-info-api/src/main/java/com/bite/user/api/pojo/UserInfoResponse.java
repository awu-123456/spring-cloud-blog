package com.bite.user.api.pojo;

import lombok.Data;

@Data
public class UserInfoResponse {
    private Integer id;
    private String userName;
    private String githubUrl;
}
