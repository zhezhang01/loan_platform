package com.wwj.srb.core.controller.api;

import com.wwj.common.result.R;
import com.wwj.common.result.ResponseEnum;
import com.wwj.common.result.exception.Assert;
import com.wwj.common.util.RegexValidateUtils;
import com.wwj.srb.base.util.JwtUtils;
import com.wwj.srb.core.pojo.vo.LoginVO;
import com.wwj.srb.core.pojo.vo.RegisterVO;
import com.wwj.srb.core.pojo.vo.UserIndexVO;
import com.wwj.srb.core.pojo.vo.UserInfoVO;
import com.wwj.srb.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Api(tags = "Membership interface")
@RestController
@RequestMapping("/api/core/userInfo")
@Slf4j
//@CrossOrigin
public class UserInfoController {

    @Resource
    private RedisTemplate redisTemplate;
    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation("Membership sign up")
    @PostMapping("/register")
    public R register(
            @RequestBody RegisterVO registerVO) {
        String mobile = registerVO.getMobile();
        String password = registerVO.getPassword();
        String code = registerVO.getCode();

        // Verify user input
        Assert.notEmpty(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        Assert.notEmpty(password, ResponseEnum.PASSWORD_NULL_ERROR);
        Assert.notEmpty(code, ResponseEnum.CODE_NULL_ERROR);
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile), ResponseEnum.MOBILE_ERROR);

        // verify the verification code
        String codeGen = (String) redisTemplate.opsForValue().get("srb:sms:code:" + mobile);
        System.out.println(code + "----" + codeGen);
        Assert.equals(code, codeGen, ResponseEnum.CODE_ERROR);

        // sign up
        userInfoService.register(registerVO);
        return R.ok().message("sign up success!");
    }

    @ApiOperation("Membership sign in")
    @PostMapping("/login")
    public R login(@RequestBody LoginVO loginVO, HttpServletRequest request) {
        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();

        // Verify the validation of user input
        Assert.notEmpty(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        Assert.notEmpty(password, ResponseEnum.PASSWORD_NULL_ERROR);

        String ip = request.getRemoteAddr();
        UserInfoVO userInfoVO = userInfoService.login(loginVO, ip);
        return R.ok().data("userInfo", userInfoVO);
    }

    @ApiOperation("Verify token")
    @GetMapping("/checkToken")
    public R checkToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        // check token
        boolean result = JwtUtils.checkToken(token);
        if (result)
            return R.ok();
        else
            return R.setResult(ResponseEnum.LOGIN_AUTH_ERROR);
    }

    @ApiOperation("Check whether the phone number has been used to sign up")
    @GetMapping("/checkMobile/{mobile}")
    public boolean checkMobile(@PathVariable String mobile) {
        return userInfoService.checkMobile(mobile);
    }

    @ApiOperation("Get user info")
    @GetMapping("/auth/getIndexUserInfo")
    public R getIndexUserInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        UserIndexVO userIndexVO = userInfoService.getIndexUserInfo(userId);
        return R.ok().data("userIndexVO", userIndexVO);
    }
}
