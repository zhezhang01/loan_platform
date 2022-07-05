package com.wwj.srb.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * borrower
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Borrower object", description = "borrower")
public class Borrower implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Number")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "user id")
    private Long userId;

    @ApiModelProperty(value = "name")
    private String name;

    @ApiModelProperty(value = "id number")
    private String idCard;

    @ApiModelProperty(value = "phone")
    private String mobile;

    @ApiModelProperty(value = "gender (1:male 0:female)")
    private Integer sex;

    @ApiModelProperty(value = "age")
    private Integer age;

    @ApiModelProperty(value = "education")
    private Integer education;

    @ApiModelProperty(value = "marriage(1:yes 0:no)")
    @TableField("is_marry")
    private Boolean marry;

    @ApiModelProperty(value = "industry")
    private Integer industry;

    @ApiModelProperty(value = "income")
    private Integer income;

    @ApiModelProperty(value = "Repayment source")
    private Integer returnSource;

    @ApiModelProperty(value = "Contact Name")
    private String contactsName;

    @ApiModelProperty(value = "Contact phone number")
    private String contactsMobile;

    @ApiModelProperty(value = "Contact relationship")
    private Integer contactsRelation;

    @ApiModelProperty(value = "status(0:Unauthorized, 1:Authenticating, 2:Approved, -1:Failure)")
    private Integer status;

    @ApiModelProperty(value = "Create time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "Update time")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "Delete status(1:deleted，0:Not deleted)")
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;


}
