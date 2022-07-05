package com.wwj.srb.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "message")
public class SmsDTO {

    @ApiModelProperty(value = "phone number")
    private String mobile;

    @ApiModelProperty(value = "message content")
    private String message;
}