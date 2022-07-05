package com.wwj.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwj.common.result.ResponseEnum;
import com.wwj.common.result.exception.Assert;
import com.wwj.srb.core.enums.LendStatusEnum;
import com.wwj.srb.core.enums.TransTypeEnum;
import com.wwj.srb.core.hfb.FormHelper;
import com.wwj.srb.core.hfb.HfbConst;
import com.wwj.srb.core.hfb.RequestHelper;
import com.wwj.srb.core.mapper.LendItemMapper;
import com.wwj.srb.core.mapper.LendMapper;
import com.wwj.srb.core.mapper.UserAccountMapper;
import com.wwj.srb.core.pojo.bo.TransFlowBO;
import com.wwj.srb.core.pojo.entity.Lend;
import com.wwj.srb.core.pojo.entity.LendItem;
import com.wwj.srb.core.pojo.vo.InvestVO;
import com.wwj.srb.core.service.*;
import com.wwj.srb.core.util.LendNoUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 标的出借记录表 服务实现类
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Service
public class LendItemServiceImpl extends ServiceImpl<LendItemMapper, LendItem> implements LendItemService {

    @Resource
    private LendMapper lendMapper;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserBindService userBindService;
    @Autowired
    private LendService lendService;
    @Autowired
    private TransFlowService transFlowService;
    @Resource
    private UserAccountMapper userAccountMapper;

    @Override
    public String commitInvest(InvestVO investVO) {

        //input verification==========================================
        Long lendId = investVO.getLendId();
        //Get trade info
        Lend lend = lendMapper.selectById(lendId);

        Assert.isTrue(lend.getStatus().intValue() == LendStatusEnum.INVEST_RUN.getStatus().intValue(), ResponseEnum.LEND_INVEST_ERROR);

        //Check if the total invest amount exceed the loan amount
        BigDecimal sum = lend.getInvestAmount().add(new BigDecimal(investVO.getInvestAmount()));
        Assert.isTrue(sum.doubleValue() <= lend.getAmount().doubleValue(), ResponseEnum.LEND_FULL_SCALE_ERROR);

        //If there is enough amount in the account,the investor can invest a loan
        Long investUserId = investVO.getInvestUserId();
        BigDecimal amount = userAccountService.getAccount(investUserId);
        Assert.isTrue(amount.doubleValue() >= Double.parseDouble(investVO.getInvestAmount()), ResponseEnum.NOT_SUFFICIENT_FUNDS_ERROR);

        //Generate trade info==========================================
        //The invest info of one trade
        LendItem lendItem = new LendItem();
        lendItem.setInvestUserId(investUserId);//investor id
        lendItem.setInvestName(investVO.getInvestName());//investor name
        String lendItemNo = LendNoUtils.getLendItemNo();
        lendItem.setLendItemNo(lendItemNo);
        lendItem.setLendId(investVO.getLendId());//trade id
        lendItem.setInvestAmount(new BigDecimal(investVO.getInvestAmount())); //invest amount
        lendItem.setLendYearRate(lend.getLendYearRate());//year rate
        lendItem.setInvestTime(LocalDateTime.now()); //invest time
        lendItem.setLendStartDate(lend.getLendStartDate()); //start time
        lendItem.setLendEndDate(lend.getLendEndDate()); //end time

        //Expected profit
        BigDecimal expectAmount = lendService.getInterestCount(
                lendItem.getInvestAmount(),
                lendItem.getLendYearRate(),
                lend.getPeriod(),
                lend.getReturnMethod());
        lendItem.setExpectAmount(expectAmount);

        //Read profit
        lendItem.setRealAmount(new BigDecimal(0));

        lendItem.setStatus(0);
        baseMapper.insert(lendItem);


        //Generate the info need to submit into third party platform
        String bindCode = userBindService.getBindCodeByUserId(investUserId);
        String benefitBindCode = userBindService.getBindCodeByUserId(lend.getUserId());

        //Generate the info need to submit into third party platform
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);
        paramMap.put("voteBindCode", bindCode);
        paramMap.put("benefitBindCode", benefitBindCode);
        paramMap.put("agentProjectCode", lend.getLendNo());//programme num.
        paramMap.put("agentProjectName", lend.getTitle());

        paramMap.put("agentBillNo", lendItemNo);//Order number
        paramMap.put("voteAmt", investVO.getInvestAmount());
        paramMap.put("votePrizeAmt", "0");
        paramMap.put("voteFeeAmt", "0");
        paramMap.put("projectAmt", lend.getAmount()); //total amount of a trade
        paramMap.put("note", "");
        paramMap.put("notifyUrl", HfbConst.INVEST_NOTIFY_URL);
        paramMap.put("returnUrl", HfbConst.INVEST_RETURN_URL);
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        String sign = RequestHelper.getSign(paramMap);
        paramMap.put("sign", sign);

        //Generate submission form
        return FormHelper.buildForm(HfbConst.INVEST_URL, paramMap);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void notify(Map<String, Object> paramMap) {
        String agentBillNo = (String) paramMap.get("agentBillNo");
        boolean result = transFlowService.isSaveTransFlow(agentBillNo);
        if (result) {
            log.warn("Idempotent return");
            return;
        }

        // Deduct invest amount from the investor's account
        String voteBindCode = (String) paramMap.get("voteBindCode");
        String voteAmt = (String) paramMap.get("voteAmt");
        userAccountMapper.updateAccount(
                voteBindCode,
                new BigDecimal("-" + voteAmt),
                new BigDecimal(voteAmt));

        // update invest record
        LendItem lendItem = this.getByLendItemNo(agentBillNo);
        lendItem.setStatus(1);
        baseMapper.updateById(lendItem);

        // update trade record
        Long lendId = lendItem.getLendId();
        Lend lend = lendMapper.selectById(lendId);
        lend.setInvestNum(lend.getInvestNum() + 1);
        lend.setInvestAmount(lend.getInvestAmount().add(lendItem.getInvestAmount()));
        lendMapper.updateById(lend);

        // add new transaction
        TransFlowBO transFlowBO = new TransFlowBO(
                agentBillNo,
                voteBindCode,
                new BigDecimal(voteAmt),
                TransTypeEnum.INVEST_LOCK,
                "programme id:" + lend.getLendNo() + ",programme name:" + lend.getTitle());
        transFlowService.saveTransFlow(transFlowBO);
    }

    @Override
    public List<LendItem> selectByLendId(Long lendId, Integer status) {
        return baseMapper.selectList(
                new LambdaQueryWrapper<LendItem>()
                        .eq(LendItem::getLendId, lendId)
                        .eq(LendItem::getStatus, status));
    }

    @Override
    public List<LendItem> selectByLendId(Long lendId) {
        return baseMapper.selectList(
                new LambdaQueryWrapper<LendItem>()
                        .eq(LendItem::getLendId, lendId));
    }

    /**
     * 根据流水号获取投资记录
     *
     * @param lendItemNo
     * @return
     */
    private LendItem getByLendItemNo(String lendItemNo) {
        return baseMapper.selectOne(
                new LambdaQueryWrapper<LendItem>()
                        .eq(LendItem::getLendItemNo, lendItemNo));
    }
}
