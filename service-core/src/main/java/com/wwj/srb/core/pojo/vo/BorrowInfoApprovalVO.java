package com.wwj.srb.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(description = "borrow info review")
public class BorrowInfoApprovalVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "status")
    private Integer status;

    @ApiModelProperty(value = "review content")
    private String content;

    @ApiModelProperty(value = "title")
    private String title;

    @ApiModelProperty(value = "year rate")
    private BigDecimal lendYearRate;

    @ApiModelProperty(value = "service fee rate")
    private BigDecimal serviceRate;

    @ApiModelProperty(value = "start date")
    private String lendStartDate;

    @ApiModelProperty(value = "description info")
    private String lendInfo;
}
