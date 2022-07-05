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
 * User basic info
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "UserInfo object", description = "User basic info")
public class UserInfo implements Serializable {

    public static final Integer STATUS_NORMAL = 1;
    public static final Integer STATUS_LOCKED = 0;
    public static final String USER_AVATAR = "https://srb-file-wwj.oss-cn-beijing.aliyuncs.com/avatar/01.jpg";

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "serial number")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "1:lender 2:borrower")
    private Integer userType;

    @ApiModelProperty(value = "phone number")
    private String mobile;

    @ApiModelProperty(value = "user password")
    private String password;

    @ApiModelProperty(value = " User nickname")
    private String nickName;

    @ApiModelProperty(value = "user name")
    private String name;

    @ApiModelProperty(value = "id number")
    private String idCard;

    @ApiModelProperty(value = "email")
    private String email;

    @ApiModelProperty(value = "wechat openid")
    private String openid;

    @ApiModelProperty(value = "profile image")
    private String headImg;

    @ApiModelProperty(value = "bind status(0:Unbound，1:bind success -1:bind failure)")
    private Integer bindStatus;

    @ApiModelProperty(value = "Borrower authentication status(0:Not certified 1:Authenticating 2:Approved -1:Authentication failure)")
    private Integer borrowAuthStatus;

    @ApiModelProperty(value = "bind code")
    private String bindCode;

    @ApiModelProperty(value = "user integral")
    private Integer integral;

    @ApiModelProperty(value = "status(0:lock 1:normal)")
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
