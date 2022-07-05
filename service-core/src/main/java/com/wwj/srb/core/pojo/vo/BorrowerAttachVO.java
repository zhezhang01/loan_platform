package com.wwj.srb.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "borrower attachment")
public class BorrowerAttachVO {

    @ApiModelProperty(value = "image type(idCard1:id front side,idCard2:id back side,house:Premises Permit,car:vehicle registration)")
    private String imageType;

    @ApiModelProperty(value = "image path")
    private String imageUrl;
}
