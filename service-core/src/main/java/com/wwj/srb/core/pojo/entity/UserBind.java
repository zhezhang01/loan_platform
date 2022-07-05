package com.wwj.srb.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * User bind form
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "UserBind object", description = "User bind form")
public class UserBind implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "serial number")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "user id")
    private Long userId;

    @ApiModelProperty(value = "user name")
    private String name;

    @ApiModelProperty(value = "id number")
    private String idCard;

    @ApiModelProperty(value = "bank account number")
    private String bankNo;

    @ApiModelProperty(value = "bank type")
    private String bankType;

    @ApiModelProperty(value = "phone number")
    private String mobile;

    @ApiModelProperty(value = "bind code")
    private String bindCode;

    @ApiModelProperty(value = "status")
    private Integer status;

    @ApiModelProperty(value = "create time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "update time")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "delete status(1:deleted, 0:not deleted)")
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;


}
