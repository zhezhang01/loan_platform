package com.wwj.srb.core.pojo.entity;

import java.math.BigDecimal;

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
 * Transaction form
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "TransFlow object", description = "Transaction form")
public class TransFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "serial number")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "user id")
    private Long userId;

    @ApiModelProperty(value = "user name")
    private String userName;

    @ApiModelProperty(value = "transaction number")
    private String transNo;

    @ApiModelProperty(value = "transaction type(1:deposit 2:Withdrawal 3:bid 4:Return of investment ...）")
    private Integer transType;

    @ApiModelProperty(value = "transaction type")
    private String transTypeName;

    @ApiModelProperty(value = "transaction amount")
    private BigDecimal transAmount;

    @ApiModelProperty(value = "annotation")
    private String memo;

    @ApiModelProperty(value = "create time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "update time")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "delete status(1:deleted, 0:not deleted)")
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;


}
