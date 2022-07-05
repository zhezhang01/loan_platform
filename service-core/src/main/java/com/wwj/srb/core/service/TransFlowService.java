package com.wwj.srb.core.service;

import com.wwj.srb.core.pojo.bo.TransFlowBO;
import com.wwj.srb.core.pojo.entity.TransFlow;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 交易流水表 服务类
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
public interface TransFlowService extends IService<TransFlow> {

    void saveTransFlow(TransFlowBO transFlowBO);
    boolean isSaveTransFlow(String agentBillNo);

    List<TransFlow> selectByUserId(Long userId);
}
