package com.wwj.srb.core.service;

import com.wwj.srb.core.pojo.entity.UserBind;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wwj.srb.core.pojo.vo.UserBindVO;

import java.util.Map;

/**
 * <p>
 * 用户绑定表 服务类
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
public interface UserBindService extends IService<UserBind> {

    /**
     * Dynamically assemble a submission form through the information entered by the foreground user,the form can submit automatically（Using script to submit）
     *
     * @param userBindVO user info
     * @param userId     user Id
     * @return Dynamic form string
     */
    String commitBindUser(UserBindVO userBindVO, Long userId);

    void notify(Map<String, Object> paramMap);

    String getBindCodeByUserId(Long userId);
}
