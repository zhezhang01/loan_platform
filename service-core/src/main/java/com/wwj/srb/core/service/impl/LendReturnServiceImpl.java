package com.wwj.srb.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwj.common.result.ResponseEnum;
import com.wwj.common.result.exception.Assert;
import com.wwj.srb.core.enums.LendStatusEnum;
import com.wwj.srb.core.enums.TransTypeEnum;
import com.wwj.srb.core.hfb.FormHelper;
import com.wwj.srb.core.hfb.HfbConst;
import com.wwj.srb.core.hfb.RequestHelper;
import com.wwj.srb.core.mapper.*;
import com.wwj.srb.core.pojo.bo.TransFlowBO;
import com.wwj.srb.core.pojo.entity.Lend;
import com.wwj.srb.core.pojo.entity.LendItem;
import com.wwj.srb.core.pojo.entity.LendItemReturn;
import com.wwj.srb.core.pojo.entity.LendReturn;
import com.wwj.srb.core.service.*;
import com.wwj.srb.core.util.LendNoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 还款记录表 服务实现类
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Service
@Slf4j
public class LendReturnServiceImpl extends ServiceImpl<LendReturnMapper, LendReturn> implements LendReturnService {

    @Resource
    private UserAccountService userAccountService;
    @Resource
    private LendMapper lendMapper;
    @Resource
    private UserBindService userBindService;
    @Resource
    private LendItemReturnService lendItemReturnService;
    @Resource
    private TransFlowService transFlowService;
    @Resource
    private UserAccountMapper userAccountMapper;
    @Resource
    private LendItemReturnMapper lendItemReturnMapper;
    @Resource
    private LendItemMapper lendItemMapper;

    @Override
    public List<LendReturn> selectByLendId(Long lendId) {
        return baseMapper.selectList(
                new LambdaQueryWrapper<LendReturn>()
                        .eq(LendReturn::getLendId, lendId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String commitReturn(Long lendReturnId, Long userId) {
        //Get return record
        LendReturn lendReturn = baseMapper.selectById(lendReturnId);

        //Check if there is enough balance in the user's account
        BigDecimal amount = userAccountService.getAccount(userId);
        Assert.isTrue(amount.doubleValue() >= lendReturn.getTotal().doubleValue(),
                ResponseEnum.NOT_SUFFICIENT_FUNDS_ERROR);

        //get borrower's code
        String bindCode = userBindService.getBindCodeByUserId(userId);
        //get lend
        Long lendId = lendReturn.getLendId();
        Lend lend = lendMapper.selectById(lendId);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);
        paramMap.put("agentGoodsName", lend.getTitle());
        paramMap.put("agentBatchNo", lendReturn.getReturnNo());
        paramMap.put("fromBindCode", bindCode);
        paramMap.put("totalAmt", lendReturn.getTotal());
        paramMap.put("note", "");
        List<Map<String, Object>> lendItemReturnDetailList = lendItemReturnService.addReturnDetail(lendReturnId);
        paramMap.put("data", JSONObject.toJSONString(lendItemReturnDetailList));
        paramMap.put("voteFeeAmt", new BigDecimal(0));
        paramMap.put("notifyUrl", HfbConst.BORROW_RETURN_NOTIFY_URL);
        paramMap.put("returnUrl", HfbConst.BORROW_RETURN_RETURN_URL);
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        String sign = RequestHelper.getSign(paramMap);
        paramMap.put("sign", sign);

        return FormHelper.buildForm(HfbConst.BORROW_RETURN_URL, paramMap);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void notify(Map<String, Object> paramMap) {
        log.info("Return successfully");

        //Return id
        String agentBatchNo = (String) paramMap.get("agentBatchNo");

        boolean result = transFlowService.isSaveTransFlow(agentBatchNo);
        if (result) {
            log.warn("Idempotent return");
            return;
        }

        //Get return data
        String voteFeeAmt = (String) paramMap.get("voteFeeAmt");
        LendReturn lendReturn = baseMapper.selectOne(
                new LambdaQueryWrapper<LendReturn>()
                        .eq(LendReturn::getReturnNo, agentBatchNo));

        //Update return status
        lendReturn.setStatus(1);
        lendReturn.setFee(new BigDecimal(voteFeeAmt));
        lendReturn.setRealReturnTime(LocalDateTime.now());
        baseMapper.updateById(lendReturn);

        //Update trade info
        Lend lend = lendMapper.selectById(lendReturn.getLendId());
        if (lendReturn.getLast()) {
            lend.setStatus(LendStatusEnum.PAY_OK.getStatus());
            lendMapper.updateById(lend);
        }

        //The amount the invest transferred
        BigDecimal totalAmt = new BigDecimal((String) paramMap.get("totalAmt"));//还款金额
        String bindCode = userBindService.getBindCodeByUserId(lend.getUserId());
        userAccountMapper.updateAccount(bindCode, totalAmt.negate(), new BigDecimal(0));

        //Borrower transaction balance
        TransFlowBO transFlowBO = new TransFlowBO(
                agentBatchNo,
                bindCode,
                totalAmt,
                TransTypeEnum.RETURN_DOWN,
                "Borrower's repayment deduction,programme num:" + lend.getLendNo() + ",programme name:" + lend.getTitle());
        transFlowService.saveTransFlow(transFlowBO);

        List<LendItemReturn> lendItemReturnList = lendItemReturnService.selectLendItemReturnList(lendReturn.getId());
        lendItemReturnList.forEach(item -> {
            //Update return status
            item.setStatus(1);
            item.setRealReturnTime(LocalDateTime.now());
            lendItemReturnMapper.updateById(item);

            //Update borrow info
            LendItem lendItem = lendItemMapper.selectById(item.getLendItemId());
            lendItem.setRealAmount(item.getInterest());
            lendItemMapper.updateById(lendItem);

            String investBindCode = userBindService.getBindCodeByUserId(item.getInvestUserId());
            userAccountMapper.updateAccount(investBindCode, item.getTotal(), new BigDecimal(0));

            TransFlowBO investTransFlowBO = new TransFlowBO(
                    LendNoUtils.getReturnItemNo(),
                    investBindCode,
                    item.getTotal(),
                    TransTypeEnum.INVEST_BACK,
                    "Received the return,programme num:" + lend.getLendNo() + ",programme name:" + lend.getTitle());
            transFlowService.saveTransFlow(investTransFlowBO);
        });
    }
}
