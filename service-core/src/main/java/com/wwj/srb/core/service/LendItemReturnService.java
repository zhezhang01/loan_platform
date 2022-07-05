package com.wwj.srb.core.service;

import com.wwj.srb.core.pojo.entity.LendItemReturn;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借回款记录表 服务类
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
public interface LendItemReturnService extends IService<LendItemReturn> {

    List<LendItemReturn> selectByLendId(Long lendId, Long userId);

    List<Map<String, Object>> addReturnDetail(Long lendReturnId);

    /**
     * Query payment collection id according to the repayment id
     * @param lendReturnId
     * @return
     */
    List<LendItemReturn> selectLendItemReturnList(Long lendReturnId);
}
