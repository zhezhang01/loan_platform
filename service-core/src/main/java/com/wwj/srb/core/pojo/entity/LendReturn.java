package com.wwj.srb.core.pojo.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDate;

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
 * Repayment record
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "LendReturn object", description = "Repayment record")
public class LendReturn implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "serial number")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "trade id")
    private Long lendId;

    @ApiModelProperty(value = "borrowInfo id")
    private Long borrowInfoId;

    @ApiModelProperty(value = "Repayment batch Number")
    private String returnNo;

    @ApiModelProperty(value = "borrower id")
    private Long userId;

    @ApiModelProperty(value = "Loan amount")
    private BigDecimal amount;

    @ApiModelProperty(value = "base amount")
    private BigDecimal baseAmount;

    @ApiModelProperty(value = "current period")
    private Integer currentPeriod;

    @ApiModelProperty(value = "year rate")
    private BigDecimal lendYearRate;

    @ApiModelProperty(value = "Repayment method 1-Equal principal and interest 2-Equal principal 3-Monthly repayment of interest and principal 4-One time repayment of principal")
    private Integer returnMethod;

    @ApiModelProperty(value = "principal")
    private BigDecimal principal;

    @ApiModelProperty(value = "interest")
    private BigDecimal interest;

    @ApiModelProperty(value = "principal and interest")
    private BigDecimal total;

    @ApiModelProperty(value = "Service fee")
    private BigDecimal fee;

    @ApiModelProperty(value = "expected repayment date")
    private LocalDate returnDate;

    @ApiModelProperty(value = "real repayment date")
    private LocalDateTime realReturnTime;

    @ApiModelProperty(value = "overdue")
    @TableField("is_overdue")
    private Boolean overdue;

    @ApiModelProperty(value = "overdue amount")
    private BigDecimal overdueTotal;

    @ApiModelProperty(value = "last repayment")
    @TableField("is_last")
    private Boolean last;

    @ApiModelProperty(value = "status(0-not returned 1-returned)")
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
