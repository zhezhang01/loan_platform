package com.wwj.srb.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Login object")
public class LoginVO {

    @ApiModelProperty(value = "User type")
    private Integer userType;

    @ApiModelProperty(value = "phone number")
    private String mobile;

    @ApiModelProperty(value = "password")
    private String password;
}
