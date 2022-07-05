package com.wwj.srb.core.controller.api;


import com.alibaba.fastjson.JSON;
import com.wwj.common.result.R;
import com.wwj.srb.base.util.JwtUtils;
import com.wwj.srb.core.hfb.RequestHelper;
import com.wwj.srb.core.service.UserAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 用户账户 前端控制器
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Api(tags = "Membership account")
@RestController
@RequestMapping("/api/core/userAccount")
@Slf4j
public class UserAccountController {

    @Resource
    private UserAccountService userAccountService;

    @ApiOperation("Deposit")
    @PostMapping("/auth/commitCharge/{chargeAmt}")
    public R commitCharge(
            @ApiParam(value = "Deposit amount", required = true)
            @PathVariable BigDecimal chargeAmt, HttpServletRequest request) {
        // get id of current user
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        // submit data
        String formStr = userAccountService.commitCharge(chargeAmt, userId);
        return R.ok().data("formStr", formStr);
    }

    @ApiOperation(value = "Deposit processing")
    @PostMapping("/notify")
    public String notify(HttpServletRequest request) {
        Map<String, Object> paramMap = RequestHelper.switchMap(request.getParameterMap());
        log.info("Deposit processing:" + JSON.toJSONString(paramMap));

        if (RequestHelper.isSignEquals(paramMap)) {
            // check if the transaction success
            if ("0001".equals(paramMap.get("resultCode"))) {
                // syn data
                return userAccountService.notify(paramMap);
            } else {
                return "success";
            }
        } else {
            return "fail";
        }
    }

    @ApiOperation("check balance")
    @GetMapping("/auth/getAccount")
    public R getAccount(HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        BigDecimal account = userAccountService.getAccount(userId);
        return R.ok().data("account", account);
    }

    @ApiOperation("user ")
    @PostMapping("/auth/commitWithdraw/{fetchAmt}")
    public R commitWithdraw(
            @ApiParam(value = "amount", required = true)
            @PathVariable BigDecimal fetchAmt, HttpServletRequest request) {

        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        String formStr = userAccountService.commitWithdraw(fetchAmt, userId);
        return R.ok().data("formStr", formStr);
    }

    @ApiOperation("recharge process")
    @PostMapping("/notifyWithdraw")
    public String notifyWithdraw(HttpServletRequest request) {
        Map<String, Object> paramMap = RequestHelper.switchMap(request.getParameterMap());
        log.info("recharge process:" + JSON.toJSONString(paramMap));

        //Verify signature
        if(RequestHelper.isSignEquals(paramMap)) {
            //Success to recharge
            if("0001".equals(paramMap.get("resultCode"))) {
                userAccountService.notifyWithdraw(paramMap);
            } else {
                log.info("Failure to recharge:" + JSON.toJSONString(paramMap));
                return "fail";
            }
        } else {
            log.info("Wrong signature" + JSON.toJSONString(paramMap));
            return "fail";
        }
        return "success";
    }
}
