package com.wwj.srb.core.controller.admin;


import com.wwj.common.result.R;
import com.wwj.srb.core.pojo.entity.UserLoginRecord;
import com.wwj.srb.core.service.UserLoginRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户登录记录表 前端控制器
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Api(tags = "Membership login info")
@RestController
@RequestMapping("/admin/core/userLoginRecord")
@Slf4j
//@CrossOrigin
public class AdminUserLoginRecordController {

    @Autowired
    private UserLoginRecordService userLoginRecordService;

    @ApiOperation("Get membership login info")
    @GetMapping("/listTop50/{userId}")
    public R listTop50(
            @ApiParam(value = "user id", required = true)
            @PathVariable("userId") Long userId) {
        List<UserLoginRecord> list = userLoginRecordService.listTop50(userId);
        return R.ok().data("list", list);
    }
}

