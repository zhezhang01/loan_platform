package com.wwj.srb.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "registry object")
public class RegisterVO {

    @ApiModelProperty(value = "user type")
    private Integer userType;

    @ApiModelProperty(value = "phone number")
    private String mobile;

    @ApiModelProperty(value = "verification code")
    private String code;

    @ApiModelProperty(value = "password")
    private String password;
}
