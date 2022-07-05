package com.wwj.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwj.common.result.ResponseEnum;
import com.wwj.common.result.exception.Assert;
import com.wwj.srb.core.enums.UserBindEnum;
import com.wwj.srb.core.hfb.FormHelper;
import com.wwj.srb.core.hfb.HfbConst;
import com.wwj.srb.core.hfb.RequestHelper;
import com.wwj.srb.core.mapper.UserBindMapper;
import com.wwj.srb.core.mapper.UserInfoMapper;
import com.wwj.srb.core.pojo.entity.UserBind;
import com.wwj.srb.core.pojo.entity.UserInfo;
import com.wwj.srb.core.pojo.vo.UserBindVO;
import com.wwj.srb.core.service.UserBindService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户绑定表 服务实现类
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Service
public class UserBindServiceImpl extends ServiceImpl<UserBindMapper, UserBind> implements UserBindService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public String commitBindUser(UserBindVO userBindVO, Long userId) {
        // Check correctness of the bind
        UserBind userBind = baseMapper.selectOne(
                new LambdaQueryWrapper<UserBind>()
                        .eq(UserBind::getIdCard, userBindVO.getIdCard())
                        .ne(UserBind::getUserId, userId));
        Assert.isNull(userBind, ResponseEnum.USER_BIND_IDCARD_EXIST_ERROR);

        // Check if the user has bind to an account
        userBind = baseMapper.selectOne(
                new LambdaQueryWrapper<UserBind>()
                        .eq(UserBind::getUserId, userId));
        if (userBind == null) {
            // If the user doesn't bind
            userBind = new UserBind();
            BeanUtils.copyProperties(userBindVO, userBind);
            userBind.setUserId(userId);
            // Set initial status
            userBind.setStatus(UserBindEnum.NO_BIND.getStatus());
            // Save user's bind info
            baseMapper.insert(userBind);
        } else {
            // If the user exists, update bind info
            BeanUtils.copyProperties(userBindVO, userBind);
            baseMapper.updateById(userBind);
        }

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);
        paramMap.put("agentUserId", userId);
        paramMap.put("idCard", userBindVO.getIdCard());
        paramMap.put("personalName", userBindVO.getName());
        paramMap.put("bankType", userBindVO.getBankType());
        paramMap.put("bankNo", userBindVO.getBankNo());
        paramMap.put("mobile", userBindVO.getMobile());

        paramMap.put("returnUrl", HfbConst.USERBIND_RETURN_URL);
        paramMap.put("notifyUrl", HfbConst.USERBIND_NOTIFY_URL);

        paramMap.put("timestamp", RequestHelper.getTimestamp());
        paramMap.put("sign", RequestHelper.getSign(paramMap));

        // Generate the form need to submit to third party platform
        return FormHelper.buildForm(HfbConst.USERBIND_URL, paramMap);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void notify(Map<String, Object> paramMap) {
        String bindCode = (String) paramMap.get("bindCode");
        String agentUserId = (String) paramMap.get("agentUserId");

        // Query userId according to userBind
        UserBind userBind = baseMapper.selectOne(
                new LambdaQueryWrapper<UserBind>()
                        .eq(UserBind::getUserId, agentUserId));
        userBind.setBindCode(bindCode);
        userBind.setStatus(UserBindEnum.BIND_OK.getStatus());
        // Update user bind form
        baseMapper.updateById(userBind);

        // Update user form
        UserInfo userInfo = userInfoMapper.selectById(agentUserId);
        userInfo.setBindCode(bindCode);
        userInfo.setName(userBind.getName());
        userInfo.setIdCard(userBind.getIdCard());
        userInfo.setBindStatus(UserBindEnum.BIND_OK.getStatus());
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public String getBindCodeByUserId(Long userId) {
        UserBind userBind = baseMapper.selectOne(
                new LambdaQueryWrapper<UserBind>()
                        .eq(UserBind::getUserId, userId));
        return userBind.getBindCode();
    }
}
