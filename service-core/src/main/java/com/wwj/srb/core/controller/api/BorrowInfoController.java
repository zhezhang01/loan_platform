package com.wwj.srb.core.controller.api;


import com.wwj.common.result.R;
import com.wwj.srb.base.util.JwtUtils;
import com.wwj.srb.core.pojo.entity.BorrowInfo;
import com.wwj.srb.core.service.BorrowInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * <p>
 * 借款信息表 前端控制器
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Api(tags = "Loan info")
@RestController
@RequestMapping("/api/core/borrowInfo")
@Slf4j
public class BorrowInfoController {

    @Autowired
    private BorrowInfoService borrowInfoService;

    @ApiOperation("Get loan amount")
    @GetMapping("/auth/getBorrowAmount")
    public R getBorrowAmount(HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        BigDecimal borrowAmount = borrowInfoService.getBorrowAmount(userId);
        return R.ok().data("borrowAmount", borrowAmount);
    }

    @ApiOperation("Submit loan request")
    @PostMapping("/auth/save")
    public R save(@RequestBody BorrowInfo borrowInfo, HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        borrowInfoService.saveBorrowInfo(borrowInfo, userId);
        return R.ok().message("submit success!");
    }

    @ApiOperation("Get loan review status")
    @GetMapping("/auth/getBorrowInfoStatus")
    public R getBorrowInfoStatus(HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        Integer status = borrowInfoService.getStatusByUserId(userId);
        return R.ok().data("borrowInfoStatus", status);
    }
}

