package com.sharedschool.backend.typehandler;

import lombok.Data;

@Data
public class FollowsInfo {
    private Long userId;
    private String username;
    private String avatar;
    private String intro;
}
