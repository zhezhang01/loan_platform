package com.wwj.srb.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(description="borrower detailed info")
public class BorrowerDetailVO {

    @ApiModelProperty(value = "user id")
    private Long userId;

    @ApiModelProperty(value = "name")
    private String name;

    @ApiModelProperty(value = "id number")
    private String idCard;

    @ApiModelProperty(value = "phone number")
    private String mobile;

    @ApiModelProperty(value = "gender")
    private String sex;

    @ApiModelProperty(value = "age")
    private Integer age;

    @ApiModelProperty(value = "education level")
    private String education;

    @ApiModelProperty(value = "marriage")
    private String marry;

    @ApiModelProperty(value = "industry")
    private String industry;

    @ApiModelProperty(value = "month income")
    private String income;

    @ApiModelProperty(value = "repayment source")
    private String returnSource;

    @ApiModelProperty(value = "contact name")
    private String contactsName;

    @ApiModelProperty(value = "contact phone")
    private String contactsMobile;

    @ApiModelProperty(value = "contact relationship")
    private String contactsRelation;

    @ApiModelProperty(value = "review status")
    private String status;

    @ApiModelProperty(value = "create time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "Borrower attachment")
    private List<BorrowerAttachVO> borrowerAttachVOList;
}