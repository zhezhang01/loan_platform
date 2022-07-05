package com.wwj.srb.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Borrower info review")
public class BorrowerApprovalVO {

    @ApiModelProperty(value = "id")
    private Long borrowerId;

    @ApiModelProperty(value = "status")
    private Integer status;

    @ApiModelProperty(value = "correctness of id info")
    private Boolean isIdCardOk;

    @ApiModelProperty(value = "correctness of house info")
    private Boolean isHouseOk;

    @ApiModelProperty(value = "correctness of vehicle info")
    private Boolean isCarOk;

    @ApiModelProperty(value = "basic integral info")
    private Integer infoIntegral;
}
