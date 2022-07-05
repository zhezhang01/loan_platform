package com.wwj.srb.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwj.common.result.exception.BusinessException;
import com.wwj.srb.core.enums.LendStatusEnum;
import com.wwj.srb.core.enums.ReturnMethodEnum;
import com.wwj.srb.core.enums.TransTypeEnum;
import com.wwj.srb.core.hfb.HfbConst;
import com.wwj.srb.core.hfb.RequestHelper;
import com.wwj.srb.core.mapper.BorrowerMapper;
import com.wwj.srb.core.mapper.LendMapper;
import com.wwj.srb.core.mapper.UserAccountMapper;
import com.wwj.srb.core.mapper.UserInfoMapper;
import com.wwj.srb.core.pojo.bo.TransFlowBO;
import com.wwj.srb.core.pojo.entity.*;
import com.wwj.srb.core.pojo.vo.BorrowInfoApprovalVO;
import com.wwj.srb.core.pojo.vo.BorrowerDetailVO;
import com.wwj.srb.core.service.*;
import com.wwj.srb.core.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 标的准备表 服务实现类
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Service
@Slf4j
public class LendServiceImpl extends ServiceImpl<LendMapper, Lend> implements LendService {

    @Autowired
    private DictService dictService;
    @Resource
    private BorrowerMapper borrowerMapper;
    @Autowired
    private BorrowerService borrowerService;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private UserAccountMapper userAccountMapper;
    @Resource
    private LendItemService lendItemService;
    @Resource
    private TransFlowService transFlowService;
    @Resource
    private LendReturnService lendReturnService;
    @Resource
    private LendItemReturnService lendItemReturnService;

    @Override
    public void createLend(BorrowInfoApprovalVO borrowInfoApprovalVO, BorrowInfo borrowInfo) {
        Lend lend = new Lend();
        lend.setUserId(borrowInfo.getUserId());
        lend.setBorrowInfoId(borrowInfo.getId());
        lend.setLendNo(LendNoUtils.getLendNo());
        lend.setTitle(borrowInfoApprovalVO.getTitle());
        lend.setAmount(borrowInfo.getAmount());
        lend.setPeriod(borrowInfo.getPeriod());
        lend.setLendYearRate(borrowInfoApprovalVO.getLendYearRate().divide(new BigDecimal(100)));
        lend.setServiceRate(borrowInfoApprovalVO.getServiceRate().divide(new BigDecimal(100)));
        lend.setReturnMethod(borrowInfo.getReturnMethod());
        lend.setLowestAmount(new BigDecimal(100));  // The minimum invest amount
        lend.setInvestAmount(new BigDecimal(0));    // The amount has invested
        lend.setInvestNum(0);    // Num of investor
        lend.setPublishDate(LocalDateTime.now());

        // The start of interest
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate lendStartDate = LocalDate.parse(borrowInfoApprovalVO.getLendStartDate(), dateTimeFormatter);
        lend.setLendStartDate(lendStartDate);
        // the due date
        LocalDate lendEndDate = lendStartDate.plusMonths(borrowInfo.getPeriod());
        lend.setLendEndDate(lendEndDate);

        // trade description
        lend.setLendInfo(borrowInfoApprovalVO.getLendInfo());
        // expected profit
        BigDecimal monthRate = lend.getServiceRate().divide(new BigDecimal(12), 8, BigDecimal.ROUND_DOWN);
        BigDecimal expectAmount = lend.getAmount().multiply(monthRate.multiply(new BigDecimal(lend.getPeriod())));
        lend.setExpectAmount(expectAmount);
        // real profit
        lend.setRealAmount(new BigDecimal(0));
        // set trade status
        lend.setStatus(LendStatusEnum.INVEST_RUN.getStatus());
        // set review time
        lend.setCheckTime(LocalDateTime.now());
        // set reviewer
        lend.setCheckAdminId(1L);
        // save into database
        baseMapper.insert(lend);
    }

    @Override
    public List<Lend> selectList() {
        List<Lend> lendList = baseMapper.selectList(null);
        lendList.forEach(lend -> {
            String returnMethod = dictService.getNameByParentDictCodeAndValue("returnMethod", lend.getReturnMethod());
            String status = LendStatusEnum.getMsgByStatus(lend.getStatus());
            lend.getParam().put("returnMethod", returnMethod);
            lend.getParam().put("status", status);
        });
        return lendList;
    }

    @Override
    public Map<String, Object> getLendDetail(Long id) {
        // query lend
        Lend lend = baseMapper.selectById(id);
        String returnMethod = dictService.getNameByParentDictCodeAndValue("returnMethod", lend.getReturnMethod());
        String status = LendStatusEnum.getMsgByStatus(lend.getStatus());
        lend.getParam().put("returnMethod", returnMethod);
        lend.getParam().put("status", status);

        // Query borrower objection
        Borrower borrower = borrowerMapper.selectOne(
                new LambdaQueryWrapper<Borrower>()
                        .eq(Borrower::getUserId, lend.getUserId()));
        BorrowerDetailVO borrowerDetailVO = borrowerService.getBorrowerDetailVOById(borrower.getId());
        // Generate result
        Map<String, Object> result = new HashMap<>();
        result.put("lend", lend);
        result.put("borrower", borrowerDetailVO);
        return result;
    }

    @Override
    public BigDecimal getInterestCount(BigDecimal invest, BigDecimal yearRate, Integer totalmonth, Integer returnMethod) {
        if (invest == null) {
            return new BigDecimal(0);
        }
        BigDecimal interestCount = null;
        // check the way of returning
        if (returnMethod.intValue() == ReturnMethodEnum.ONE.getMethod()) {
            interestCount = Amount1Helper.getInterestCount(invest, yearRate, totalmonth);
        } else if (returnMethod.intValue() == ReturnMethodEnum.TWO.getMethod()) {
            interestCount = Amount2Helper.getInterestCount(invest, yearRate, totalmonth);
        } else if (returnMethod.intValue() == ReturnMethodEnum.THREE.getMethod()) {
            interestCount = Amount3Helper.getInterestCount(invest, yearRate, totalmonth);
        } else if (returnMethod.intValue() == ReturnMethodEnum.FOUR.getMethod()) {
            interestCount = Amount4Helper.getInterestCount(invest, yearRate, totalmonth);
        }
        return interestCount;
    }

    @Override
    public void makeLoan(Long id) {
        // Get trade info
        Lend lend = baseMapper.selectById(id);
        // invoke third party platform lending interface
        Map<String, Object> map = new HashMap<>();
        map.put("agentId", HfbConst.AGENT_ID);
        map.put("agentProjectCode", lend.getLendNo());
        map.put("agentBillNo", LendNoUtils.getLoanNo());
        // Calculate month rate
        BigDecimal monthRate = lend.getServiceRate().divide(new BigDecimal(12), 8, BigDecimal.ROUND_DOWN);
        // The service fee of the platform
        BigDecimal realAmount = lend.getInvestAmount().multiply(monthRate).multiply(new BigDecimal(lend.getPeriod()));
        map.put("mchFee", realAmount);
        map.put("timestamp", RequestHelper.getTimestamp());
        map.put("sign", RequestHelper.getSign(map));

        JSONObject result = RequestHelper.sendRequest(map, HfbConst.MAKE_LOAD_URL);
        log.info("lending result:" + result.toJSONString());
        // if lending failure......
        if (!"0000".equals(result.getString("resultCode"))) {
            throw new BusinessException(result.getString("resultMsg"));
        }

        lend.setRealAmount(realAmount); // platform profit
        lend.setStatus(LendStatusEnum.PAY_RUN.getStatus()); // trade status
        lend.setPaymentTime(LocalDateTime.now());
        baseMapper.updateById(lend);

        Long userId = lend.getUserId();
        UserInfo userInfo = userInfoMapper.selectById(userId);
        String bindCode = userInfo.getBindCode();
        BigDecimal voteAmt = new BigDecimal(result.getString("voteAmt"));
        userAccountMapper.updateAccount(bindCode, voteAmt, new BigDecimal(0));

        // add borrow transaction balance
        TransFlowBO transFlowBO = new TransFlowBO(
                result.getString("agentBillNo"),
                bindCode,
                voteAmt,
                TransTypeEnum.BORROW_BACK,
                "Starting lending,the programme num:" + lend.getLendNo() + ",programme name:" + lend.getTitle());

        transFlowService.saveTransFlow(transFlowBO);

        List<LendItem> lendItemList = lendItemService.selectByLendId(id, 1);
        lendItemList.stream().forEach(item -> {
            Long investUserId = item.getInvestUserId();
            // investor
            UserInfo investUserInfo = userInfoMapper.selectById(investUserId);
            String investBindCode = investUserInfo.getBindCode();
            BigDecimal investAmount = item.getInvestAmount();
            userAccountMapper.updateAccount(investBindCode, new BigDecimal(0), investAmount.negate());
            // add investor transaction balance
            TransFlowBO investTransFlowBO = new TransFlowBO(
                    LendNoUtils.getTransNo(),
                    investBindCode,
                    investAmount,
                    TransTypeEnum.INVEST_UNLOCK,
                    "Starting lending,the programme num:" + lend.getLendNo() + ",programme name:" + lend.getTitle());

        });
        // Generate borrower and lender
        this.repaymentPlan(lend);
    }

    /**
     * 还款计划
     *
     * @param lend
     */
    private void repaymentPlan(Lend lend) {

        //Return plan list
        List<LendReturn> lendReturnList = new ArrayList<>();

        //Generate return plan according to time
        int len = lend.getPeriod();
        for (int i = 1; i <= len; i++) {

            //Create return plan objection
            LendReturn lendReturn = new LendReturn();
            lendReturn.setReturnNo(LendNoUtils.getReturnNo());
            lendReturn.setLendId(lend.getId());
            lendReturn.setBorrowInfoId(lend.getBorrowInfoId());
            lendReturn.setUserId(lend.getUserId());
            lendReturn.setAmount(lend.getAmount());
            lendReturn.setBaseAmount(lend.getInvestAmount());
            lendReturn.setLendYearRate(lend.getLendYearRate());
            lendReturn.setCurrentPeriod(i);//current period
            lendReturn.setReturnMethod(lend.getReturnMethod());

            //			lendReturn.setPrincipal();
            //			lendReturn.setInterest();
            //			lendReturn.setTotal();

            lendReturn.setFee(new BigDecimal(0));
            lendReturn.setReturnDate(lend.getLendStartDate().plusMonths(i)); //第二个月开始还款
            lendReturn.setOverdue(false);
            if (i == len) { //last month
                //last time to return
                lendReturn.setLast(true);
            } else {
                lendReturn.setLast(false);
            }
            lendReturn.setStatus(0);
            lendReturnList.add(lendReturn);
        }
        lendReturnService.saveBatch(lendReturnList);

        Map<Integer, Long> lendReturnMap = lendReturnList.stream().collect(
                Collectors.toMap(LendReturn::getCurrentPeriod, LendReturn::getId)
        );

        //======================================================
        //==Get the info all investor and generate return plan==
        //======================================================
        //Return plan list
        List<LendItemReturn> lendItemReturnAllList = new ArrayList<>();
        //Get the invest record
        List<LendItem> lendItemList = lendItemService.selectByLendId(lend.getId(), 1);
        for (LendItem lendItem : lendItemList) {
            //create return plan list
            List<LendItemReturn> lendItemReturnList = this.returnInvest(lendItem.getId(), lendReturnMap, lend);
            lendItemReturnAllList.addAll(lendItemReturnList);
        }

        //Update return plan
        for (LendReturn lendReturn : lendReturnList) {
            BigDecimal sumPrincipal = lendItemReturnAllList.stream()
                    .filter(item -> item.getLendReturnId().longValue() == lendReturn.getId().longValue())
                    .map(LendItemReturn::getPrincipal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal sumInterest = lendItemReturnAllList.stream()
                    .filter(item -> item.getLendReturnId().longValue() == lendReturn.getId().longValue())
                    .map(LendItemReturn::getInterest)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal sumTotal = lendItemReturnAllList.stream()
                    .filter(item -> item.getLendReturnId().longValue() == lendReturn.getId().longValue())
                    .map(LendItemReturn::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            lendReturn.setPrincipal(sumPrincipal);
            lendReturn.setInterest(sumInterest);
            lendReturn.setTotal(sumTotal);
        }
        lendReturnService.updateBatchById(lendReturnList);
    }

    /**
     * 回款计划
     *
     * @param lendItemId
     * @param lendReturnMap
     * @param lend
     * @return
     */
    public List<LendItemReturn> returnInvest(Long lendItemId, Map<Integer, Long> lendReturnMap, Lend lend) {

        LendItem lendItem = lendItemService.getById(lendItemId);

        //Invest amount
        BigDecimal amount = lendItem.getInvestAmount();
        //Year rate
        BigDecimal yearRate = lendItem.getLendYearRate();
        //investment periods
        int totalMonth = lend.getPeriod();

        Map<Integer, BigDecimal> mapInterest = null;
        Map<Integer, BigDecimal> mapPrincipal = null;

        //Calculate interest and principal
        if (lend.getReturnMethod().intValue() == ReturnMethodEnum.ONE.getMethod()) {
            //interest
            mapInterest = Amount1Helper.getPerMonthInterest(amount, yearRate, totalMonth);
            //principal
            mapPrincipal = Amount1Helper.getPerMonthPrincipal(amount, yearRate, totalMonth);
        } else if (lend.getReturnMethod().intValue() == ReturnMethodEnum.TWO.getMethod()) {
            mapInterest = Amount2Helper.getPerMonthInterest(amount, yearRate, totalMonth);
            mapPrincipal = Amount2Helper.getPerMonthPrincipal(amount, yearRate, totalMonth);
        } else if (lend.getReturnMethod().intValue() == ReturnMethodEnum.THREE.getMethod()) {
            mapInterest = Amount3Helper.getPerMonthInterest(amount, yearRate, totalMonth);
            mapPrincipal = Amount3Helper.getPerMonthPrincipal(amount, yearRate, totalMonth);
        } else {
            mapInterest = Amount4Helper.getPerMonthInterest(amount, yearRate, totalMonth);
            mapPrincipal = Amount4Helper.getPerMonthPrincipal(amount, yearRate, totalMonth);
        }

        List<LendItemReturn> lendItemReturnList = new ArrayList<>();
        for (Map.Entry<Integer, BigDecimal> entry : mapInterest.entrySet()) {
            Integer currentPeriod = entry.getKey();
            //Get return plan id
            Long lendReturnId = lendReturnMap.get(currentPeriod);

            LendItemReturn lendItemReturn = new LendItemReturn();
            lendItemReturn.setLendReturnId(lendReturnId);
            lendItemReturn.setLendItemId(lendItemId);
            lendItemReturn.setInvestUserId(lendItem.getInvestUserId());
            lendItemReturn.setLendId(lendItem.getLendId());
            lendItemReturn.setInvestAmount(lendItem.getInvestAmount());
            lendItemReturn.setLendYearRate(lend.getLendYearRate());
            lendItemReturn.setCurrentPeriod(currentPeriod);
            lendItemReturn.setReturnMethod(lend.getReturnMethod());
            //Calculate principal
            if (lendItemReturnList.size() > 0 && currentPeriod.intValue() == lend.getPeriod().intValue()) {
                BigDecimal sumPrincipal = lendItemReturnList.stream()
                        .map(LendItemReturn::getPrincipal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal lastPrincipal = lendItem.getInvestAmount().subtract(sumPrincipal);
                lendItemReturn.setPrincipal(lastPrincipal);

                // interest
                BigDecimal sumInterest = lendItemReturnList.stream()
                        .map(LendItemReturn::getInterest)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal lastInterest = lendItem.getExpectAmount().subtract(sumInterest);
                lendItemReturn.setInterest(lastInterest);
            } else {
                lendItemReturn.setPrincipal(mapPrincipal.get(currentPeriod));
                lendItemReturn.setInterest(mapInterest.get(currentPeriod));
            }

            // Total return amount
            lendItemReturn.setTotal(lendItemReturn.getPrincipal().add(lendItemReturn.getInterest()));
            lendItemReturn.setFee(new BigDecimal("0"));
            lendItemReturn.setReturnDate(lend.getLendStartDate().plusMonths(currentPeriod));
            //check if overdue
            lendItemReturn.setOverdue(false);
            lendItemReturn.setStatus(0);

            lendItemReturnList.add(lendItemReturn);
        }
        lendItemReturnService.saveBatch(lendItemReturnList);

        return lendItemReturnList;
    }
}
