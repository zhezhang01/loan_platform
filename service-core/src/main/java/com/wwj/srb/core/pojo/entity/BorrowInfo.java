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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Loan information sheet
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "BorrowInfo object", description = "Loan information sheet")
public class BorrowInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "number")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "borrower id")
    private Long userId;

    @ApiModelProperty(value = "Loan amount")
    private BigDecimal amount;

    @ApiModelProperty(value = "Loan period")
    private Integer period;

    @ApiModelProperty(value = "year rate")
    private BigDecimal borrowYearRate;

    @ApiModelProperty(value = "Repayment method 1-Equal principal and interest 2-Equal principal 3-Monthly repayment of interest and principal 4-One time repayment of principal")
    private Integer returnMethod;

    @ApiModelProperty(value = "Purpose of funds")
    private Integer moneyUse;

    @ApiModelProperty(value = "Audit status(0:not submitted, 1:under reviewing, 2:Approved, -1:request denial)")
    private Integer status;

    @ApiModelProperty(value = "create time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "update time")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "delete status(1:deleted，0:not deleted)")
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;

    // Extended fields
    @ApiModelProperty(value = "name")
    @TableField(exist = false)
    private String name;

    @ApiModelProperty(value = "phone number")
    @TableField(exist = false)
    private String mobile;

    @ApiModelProperty(value = "others")
    @TableField(exist = false)
    private Map<String, Object> param = new HashMap<>();
}
