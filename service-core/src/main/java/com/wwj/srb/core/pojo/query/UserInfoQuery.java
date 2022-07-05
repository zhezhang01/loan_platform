package com.wwj.srb.core.pojo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Member search object")
public class UserInfoQuery {

    @ApiModelProperty(value = "Phone Number")
    private String mobile;

    @ApiModelProperty(value = "Status")
    private String status;

    @ApiModelProperty(value = "User type 1:lender 2:borrower")
    private String userType;
}
