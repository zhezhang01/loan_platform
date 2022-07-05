package com.wwj.srb.core.controller.admin;


import com.wwj.common.result.R;
import com.wwj.srb.core.pojo.entity.Lend;
import com.wwj.srb.core.service.LendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的准备表 前端控制器
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Api(tags = "trade management")
@RestController
@RequestMapping("/admin/core/lend")
@Slf4j
public class AdminLendController {

    @Autowired
    private LendService lendService;

    @ApiOperation("trade list")
    @GetMapping("/list")
    public R list() {
        List<Lend> lendList = lendService.selectList();
        return R.ok().data("list", lendList);
    }

    @ApiOperation("Get trade info")
    @GetMapping("/show/{id}")
    public R show(
            @ApiParam(value = "trade id", required = true)
            @PathVariable Long id) {
        Map<String, Object> result = lendService.getLendDetail(id);
        return R.ok().data("lendDetail", result);
    }

    @ApiOperation("moneylending")
    @GetMapping("/makeLoan/{id}")
    public R makeLoan(
            @ApiParam(value = "trade id", required = true)
            @PathVariable("id") Long id) {
        lendService.makeLoan(id);
        return R.ok().message("Transaction finished");
    }
}

