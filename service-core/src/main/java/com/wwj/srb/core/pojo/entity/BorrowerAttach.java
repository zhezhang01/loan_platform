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
 * Borrower uploads resource table
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "BorrowerAttach object", description = "Borrower uploads resource table")
public class BorrowerAttach implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "number")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "borrower id")
    private Long borrowerId;

    @ApiModelProperty(value = "picture type(idCard1:Front of ID card,idCard2:Reverse side of ID card,house:Property ownership certificate,car:vehicle registration)")
    private String imageType;

    @ApiModelProperty(value = "picture upload path")
    private String imageUrl;

    @ApiModelProperty(value = "picture name")
    private String imageName;

    @ApiModelProperty(value = "create time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "update time")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "delete status(1:deleted,0:not deleted)")
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;


}
