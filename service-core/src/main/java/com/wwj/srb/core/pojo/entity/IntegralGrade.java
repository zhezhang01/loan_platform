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

/**
 * <p>
 * Integral grade table
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "IntegralGrade object", description = "Integral grade table")
public class IntegralGrade implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "serial number")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "Start of integral interval")
    private Integer integralStart;

    @ApiModelProperty(value = "End of integral interval")
    private Integer integralEnd;

    @ApiModelProperty(value = "Loan limit")
    private BigDecimal borrowAmount;

    @ApiModelProperty(value = "create time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "update time")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "delete status(1:deleted, 0:not deleted)")
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;


}
