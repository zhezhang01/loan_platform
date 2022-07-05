package com.wwj.srb.core.pojo.vo;

import com.wwj.srb.core.pojo.entity.BorrowerAttach;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "Borrower verification info")
public class BorrowerVO {

    @ApiModelProperty(value = "gender(1:male 0:female)")
    private Integer sex;

    @ApiModelProperty(value = "age")
    private Integer age;

    @ApiModelProperty(value = "education level")
    private Integer education;

    @ApiModelProperty(value = "marriage(1:yes 0:no)")
    private Boolean marry;

    @ApiModelProperty(value = "industry")
    private Integer industry;

    @ApiModelProperty(value = "monthly income")
    private Integer income;

    @ApiModelProperty(value = "repayment source")
    private Integer returnSource;

    @ApiModelProperty(value = "contact name")
    private String contactsName;

    @ApiModelProperty(value = "contact phone number")
    private String contactsMobile;

    @ApiModelProperty(value = "contact relationship")
    private Integer contactsRelation;

    @ApiModelProperty(value = "borrower attachment")
    private List<BorrowerAttach> borrowerAttachList;
}
