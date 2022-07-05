package com.wwj.srb.core.service;

import com.wwj.srb.core.pojo.entity.UserLoginRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户登录记录表 服务类
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
public interface UserLoginRecordService extends IService<UserLoginRecord> {

    /**
     * Query the first 50 user logs
     *
     * @param userId User id
     * @return Log list
     */
    List<UserLoginRecord> listTop50(Long userId);
}
