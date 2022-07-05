package com.wwj.srb.core.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 积分等级表 前端控制器
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Api(tags = "Credit level interface")
@RestController
@RequestMapping("/api/core/integralGrade")
public class IntegralGradeController {

    @ApiOperation("Test interface")
    @GetMapping("/test")
    public String test() {
        return "test";
    }
}

