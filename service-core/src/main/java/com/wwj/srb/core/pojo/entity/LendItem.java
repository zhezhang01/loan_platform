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

/**
 * <p>
 * Lending record
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "LendItem object", description = "Lending record")
public class LendItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "serial number")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "invest number")
    private String lendItemNo;

    @ApiModelProperty(value = "trade id")
    private Long lendId;

    @ApiModelProperty(value = "invest user id")
    private Long investUserId;

    @ApiModelProperty(value = "investor name")
    private String investName;

    @ApiModelProperty(value = "invest amount")
    private BigDecimal investAmount;

    @ApiModelProperty(value = "year rate")
    private BigDecimal lendYearRate;

    @ApiModelProperty(value = "invest time")
    private LocalDateTime investTime;

    @ApiModelProperty(value = "start date")
    private LocalDate lendStartDate;

    @ApiModelProperty(value = "end date")
    private LocalDate lendEndDate;

    @ApiModelProperty(value = "expected profit")
    private BigDecimal expectAmount;

    @ApiModelProperty(value = "real profit")
    private BigDecimal realAmount;

    @ApiModelProperty(value = "status（0:default 1:paid 2:returned）")
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
