package com.wwj.srb.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "invest info")
public class InvestVO {

    private Long lendId;

    /**
     * invest amount
     */
    private String investAmount;


    /**
     * user id
     */
    private Long investUserId;

    /**
     * user name
     */
    private String investName;
}
