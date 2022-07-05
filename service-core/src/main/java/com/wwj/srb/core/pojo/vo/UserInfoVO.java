package com.wwj.srb.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "User info object")
public class UserInfoVO {

    @ApiModelProperty(value = "User name")
    private String name;

    @ApiModelProperty(value = "User nickname")
    private String nickName;

    @ApiModelProperty(value = "User profile image")
    private String headImg;

    @ApiModelProperty(value = "phone number")
    private String mobile;

    @ApiModelProperty(value = "User type 1:lender 2:borrower")
    private Integer userType;

    @ApiModelProperty(value = "JWT visit token")
    private String token;
}
