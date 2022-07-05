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
 * data dict
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Dict object", description = "data dict")
public class Dict implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "parent id")
    private Long parentId;

    @ApiModelProperty(value = "name")
    private String name;

    @ApiModelProperty(value = "value")
    private Integer value;

    @ApiModelProperty(value = "code")
    private String dictCode;

    @ApiModelProperty(value = "create time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "update time")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "delete status(0:Cannot delete 1:Can be deleted)")
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;

    @TableField(exist = false) // extended field
    private Boolean hasChildren;
}
