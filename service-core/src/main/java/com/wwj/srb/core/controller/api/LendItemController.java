package com.wwj.srb.core.controller.api;


import com.alibaba.fastjson.JSON;
import com.wwj.common.result.R;
import com.wwj.srb.base.util.JwtUtils;
import com.wwj.srb.core.hfb.RequestHelper;
import com.wwj.srb.core.pojo.entity.LendItem;
import com.wwj.srb.core.pojo.vo.InvestVO;
import com.wwj.srb.core.service.LendItemService;
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
 * 标的出借记录表 前端控制器
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Api(tags = "Trade invest")
@RestController
@RequestMapping("/api/core/lendItem")
@Slf4j
public class LendItemController {

    @Resource
    LendItemService lendItemService;

    @ApiOperation("Member submit invest data")
    @PostMapping("/auth/commitInvest")
    public R commitInvest(@RequestBody InvestVO investVO, HttpServletRequest request) {

        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        String userName = JwtUtils.getUserName(token);
        investVO.setInvestUserId(userId);
        investVO.setInvestName(userName);

        //submit deposit form
        String formStr = lendItemService.commitInvest(investVO);
        return R.ok().data("formStr", formStr);
    }

    @ApiOperation("Member fetch invest info")
    @PostMapping("/notify")
    public String notify(HttpServletRequest request) {

        Map<String, Object> paramMap = RequestHelper.switchMap(request.getParameterMap());
        log.info("Member fetch invest info:" + JSON.toJSONString(paramMap));

        //校验签名 P2pInvestNotifyVo
        if(RequestHelper.isSignEquals(paramMap)) {
            if("0001".equals(paramMap.get("resultCode"))) {
                lendItemService.notify(paramMap);
            } else {
                log.info("Failure to invest:" + JSON.toJSONString(paramMap));
                return "fail";
            }
        } else {
            log.info("incorrect signature:" + JSON.toJSONString(paramMap));
            return "fail";
        }
        return "success";
    }

    @ApiOperation("Get list")
    @GetMapping("/list/{lendId}")
    public R list(
            @ApiParam(value = "trade id", required = true)
            @PathVariable Long lendId) {
        List<LendItem> list = lendItemService.selectByLendId(lendId);
        return R.ok().data("list", list);
    }
}