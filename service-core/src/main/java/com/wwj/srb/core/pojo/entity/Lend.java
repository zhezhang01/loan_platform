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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Preparation form
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Lend object", description = "Preparation form")
public class Lend implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "serial number")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "borrower id")
    private Long userId;

    @ApiModelProperty(value = "borrowInfo id")
    private Long borrowInfoId;

    @ApiModelProperty(value = "trade id")
    private String lendNo;

    @ApiModelProperty(value = "title")
    private String title;

    @ApiModelProperty(value = "trade amount")
    private BigDecimal amount;

    @ApiModelProperty(value = "Number of investment periods")
    private Integer period;

    @ApiModelProperty(value = "year rate")
    private BigDecimal lendYearRate;

    @ApiModelProperty(value = "platform service fee")
    private BigDecimal serviceRate;

    @ApiModelProperty(value = "repayment method")
    private Integer returnMethod;

    @ApiModelProperty(value = "lowest invest amount")
    private BigDecimal lowestAmount;

    @ApiModelProperty(value = "invested amount")
    private BigDecimal investAmount;

    @ApiModelProperty(value = "Number of investors")
    private Integer investNum;

    @ApiModelProperty(value = "Release date")
    private LocalDateTime publishDate;

    @ApiModelProperty(value = "start date")
    private LocalDate lendStartDate;

    @ApiModelProperty(value = "end date")
    private LocalDate lendEndDate;

    @ApiModelProperty(value = "illustration")
    private String lendInfo;

    @ApiModelProperty(value = "expected profit")
    private BigDecimal expectAmount;

    @ApiModelProperty(value = "real profit")
    private BigDecimal realAmount;

    @ApiModelProperty(value = "status")
    private Integer status;

    @ApiModelProperty(value = "review time")
    private LocalDateTime checkTime;

    @ApiModelProperty(value = "Audit user id")
    private Long checkAdminId;

    @ApiModelProperty(value = "lend time")
    private LocalDateTime paymentTime;

    @ApiModelProperty(value = "lender id")
    private LocalDateTime paymentAdminId;

    @ApiModelProperty(value = "create time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "update time")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "delete status(1:deleted, 0:not deleted)")
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;

    @ApiModelProperty(value = "others")
    @TableField(exist = false)
    private Map<String, Object> param = new HashMap<>();
}
