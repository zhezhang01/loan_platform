package com.wwj.srb.sms.controller.api;

import com.wwj.common.result.R;
import com.wwj.common.result.ResponseEnum;
import com.wwj.common.result.exception.Assert;
import com.wwj.common.util.RandomUtils;
import com.wwj.common.util.RegexValidateUtils;
import com.wwj.srb.sms.client.CoreUserInfoClient;
import com.wwj.srb.sms.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/sms")
@Api(tags = "message management")
//@CrossOrigin
@Slf4j
public class ApiSmsController {

    @Autowired
    private SmsService smsService;
    @Resource
    private RedisTemplate redisTemplate;
    @Autowired
    private CoreUserInfoClient coreUserInfoClient;

    @ApiOperation("Get verification code")
    @GetMapping("/send/{mobile}")
    public R send(
            @ApiParam(value = "phone number", required = true)
            @PathVariable String mobile) {
        // Check if the phone number is empty
        Assert.notEmpty(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        // Check if the phone number is legal
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile), ResponseEnum.MOBILE_ERROR);

        // Check whether the mobile phone number has been registered (this is a mirco service remote call)
        boolean result = coreUserInfoClient.checkMobile(mobile);
        Assert.isTrue(!result, ResponseEnum.MOBILE_EXIST_ERROR);

        Map<String, Object> map = new HashMap<>();
        String code = RandomUtils.getFourBitRandom();
        map.put("code", code);
//        smsService.send(mobile, SmsProperties.TEMPLATE_CODE, map);

        // save the code into Redis,Expiration time is 5 minutes
        redisTemplate.opsForValue().set("srb:sms:code:" + mobile, code, 5, TimeUnit.MINUTES);
        return R.ok().message("sending successfully");
    }
}
