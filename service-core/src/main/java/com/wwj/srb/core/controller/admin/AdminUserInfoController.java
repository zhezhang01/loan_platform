package com.wwj.srb.core.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wwj.common.result.R;
import com.wwj.srb.core.pojo.entity.UserInfo;
import com.wwj.srb.core.pojo.query.UserInfoQuery;
import com.wwj.srb.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Membership management")
@RestController
@RequestMapping("/admin/core/userInfo")
@Slf4j
//@CrossOrigin
public class AdminUserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation("Get membership list")
    @GetMapping("/list/{page}/{limit}")
    public R listPage(
            @ApiParam(value = "current page", required = true)
            @PathVariable Long page,
            @ApiParam(value = "records pre page", required = true)
            @PathVariable Long limit,
            @ApiParam(value = "Search objection", required = false)
                    UserInfoQuery userInfoQuery) {
        Page<UserInfo> pageParam = new Page<>(page, limit);
        IPage<UserInfo> pageModel = userInfoService.listPage(pageParam, userInfoQuery);
        return R.ok().data("pageModel", pageModel);
    }

    @ApiOperation("Lock or unlock user")
    @PutMapping("/lock/{id}/{status}")
    public R lock(
            @ApiParam(value = "user id", required = true)
            @PathVariable("id") Long id,
            @ApiParam(value = "Lock status(0：lock 1：normal)", required = true)
            @PathVariable("status") Integer status) {
        userInfoService.lock(id, status);
        return R.ok().message(status == 1 ? "Unlock success" : "Lock success");
    }
}
