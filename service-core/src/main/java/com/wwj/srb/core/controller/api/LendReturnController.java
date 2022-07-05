package com.wwj.srb.core.controller.api;


import com.alibaba.fastjson.JSON;
import com.wwj.common.result.R;
import com.wwj.srb.base.util.JwtUtils;
import com.wwj.srb.core.hfb.RequestHelper;
import com.wwj.srb.core.pojo.entity.LendReturn;
import com.wwj.srb.core.service.LendReturnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 还款记录表 前端控制器
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Api(tags = "Repayment Plan")
@RestController
@RequestMapping("/api/core/lendReturn")
@Slf4j
public class LendReturnController {

    @Resource
    private LendReturnService lendReturnService;

    @ApiOperation("Get list")
    @GetMapping("/list/{lendId}")
    public R list(
            @ApiParam(value = "trade id", required = true)
            @PathVariable Long lendId) {
        List<LendReturn> list = lendReturnService.selectByLendId(lendId);
        return R.ok().data("list", list);
    }

    @ApiOperation("User repayment")
    @PostMapping("/auth/commitReturn/{lendReturnId}")
    public R commitReturn(
            @ApiParam(value = "repayment plan id", required = true)
            @PathVariable Long lendReturnId, HttpServletRequest request) {

        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        String formStr = lendReturnService.commitReturn(lendReturnId, userId);
        return R.ok().data("formStr", formStr);
    }

    @ApiOperation("Repayment process")
    @PostMapping("/notifyUrl")
    public String notifyUrl(HttpServletRequest request) {

        Map<String, Object> paramMap = RequestHelper.switchMap(request.getParameterMap());
        log.info("Repayment processing:" + JSON.toJSONString(paramMap));

        //Verify signature
        if(RequestHelper.isSignEquals(paramMap)) {
            if("0001".equals(paramMap.get("resultCode"))) {
                lendReturnService.notify(paramMap);
            } else {
                log.info("Repayment failure:" + JSON.toJSONString(paramMap));
                return "fail";
            }
        } else {
            log.info("Wrong signature:" + JSON.toJSONString(paramMap));
            return "fail";
        }
        return "success";
    }
}

