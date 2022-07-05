package com.wwj.srb.core.controller.api;


import com.alibaba.fastjson.JSON;
import com.wwj.common.result.R;
import com.wwj.srb.base.util.JwtUtils;
import com.wwj.srb.core.hfb.RequestHelper;
import com.wwj.srb.core.pojo.vo.UserBindVO;
import com.wwj.srb.core.service.UserBindService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 用户绑定表 前端控制器
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Api(tags = "Membership account bind")
@RestController
@RequestMapping("/api/core/userBind")
@Slf4j
public class UserBindController {

    @Autowired
    private UserBindService userBindService;

    @ApiOperation("Account bind submitted data")
    @PostMapping("/auth/bind")
    public R bind(@RequestBody UserBindVO userBindVO, HttpServletRequest request) {
        // get token from header and verify the info
        String token = request.getHeader("token");
        // Get userid from token
        Long userId = JwtUtils.getUserId(token);
        // bind account according to userId
        String formStr = userBindService.commitBindUser(userBindVO, userId);
        return R.ok().data("formStr", formStr);
    }

    @ApiOperation("Bind account")
    @PostMapping("/notify")
    public String notify(HttpServletRequest request) {
        // Data from hrb
        Map<String, Object> paramMap = RequestHelper.switchMap(request.getParameterMap());
        boolean result = RequestHelper.isSignEquals(paramMap);
        // Verified signature
        if (!result) {
            log.error("Cannot verify the signature:" + JSON.toJSONString(paramMap));
            return "fail";
        }
        log.info("Verified signature,start binding......");
        userBindService.notify(paramMap);
        return "success";
    }
}

