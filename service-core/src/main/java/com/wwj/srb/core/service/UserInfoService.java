package com.wwj.srb.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wwj.srb.core.pojo.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wwj.srb.core.pojo.query.UserInfoQuery;
import com.wwj.srb.core.pojo.vo.LoginVO;
import com.wwj.srb.core.pojo.vo.RegisterVO;
import com.wwj.srb.core.pojo.vo.UserIndexVO;
import com.wwj.srb.core.pojo.vo.UserInfoVO;

/**
 * <p>
 * 用户基本信息 服务类
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * User sign up
     *
     * @param registerVO registration information
     */
    void register(RegisterVO registerVO);

    /**
     * User Login
     *
     * @param loginVO user input
     * @param ip      login ip
     * @return user info object
     */
    UserInfoVO login(LoginVO loginVO, String ip);


    IPage<UserInfo> listPage(Page<UserInfo> pageParam, UserInfoQuery userInfoQuery);

    /**
     * User Lock
     *
     * @param id     Locked user's id
     * @param status
     */
    void lock(Long id, Integer status);

    /**
     * Check if the phone number has been registered
     *
     * @param mobile phone number
     * @return return if has been registered，true：yes；false：no
     */
    boolean checkMobile(String mobile);

    UserIndexVO getIndexUserInfo(Long userId);

    String getMobileByBindCode(String bindCode);
}
