package com.wwj.srb.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "Account bind")
@Data
public class UserBindVO {

    @ApiModelProperty(value = "User id number")
    private String idCard;

    @ApiModelProperty(value = "User name")
    private String name;

    @ApiModelProperty(value = "bank type")
    private String bankType;

    @ApiModelProperty(value = "bank account")
    private String bankNo;

    @ApiModelProperty(value = "phone number")
    private String mobile;

}
