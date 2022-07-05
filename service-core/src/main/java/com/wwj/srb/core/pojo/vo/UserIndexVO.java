package com.wwj.srb.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "Home page user info")
public class UserIndexVO {

    @ApiModelProperty(value = "User id")
    private Long userId;

    @ApiModelProperty(value = "User name")
    private String name;

    @ApiModelProperty(value = "User nickname")
    private String nickName;

    @ApiModelProperty(value = "1:lender 2:borrower")
    private Integer userType;

    @ApiModelProperty(value = "User profile image")
    private String headImg;

    @ApiModelProperty(value = "bind status(0:Not bind,1:success -1:failure)")
    private Integer bindStatus;

    @ApiModelProperty(value = "Available amount")
    private BigDecimal amount;

    @ApiModelProperty(value = "Frozen amount")
    private BigDecimal freezeAmount;

    @ApiModelProperty(value = "Last sign in time")
    private LocalDateTime lastLoginTime;

}