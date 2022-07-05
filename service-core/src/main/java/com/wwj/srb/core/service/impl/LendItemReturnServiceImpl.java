package com.wwj.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwj.srb.core.mapper.LendItemMapper;
import com.wwj.srb.core.mapper.LendItemReturnMapper;
import com.wwj.srb.core.mapper.LendMapper;
import com.wwj.srb.core.mapper.LendReturnMapper;
import com.wwj.srb.core.pojo.entity.Lend;
import com.wwj.srb.core.pojo.entity.LendItem;
import com.wwj.srb.core.pojo.entity.LendItemReturn;
import com.wwj.srb.core.pojo.entity.LendReturn;
import com.wwj.srb.core.service.LendItemReturnService;
import com.wwj.srb.core.service.UserBindService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借回款记录表 服务实现类
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Service
public class LendItemReturnServiceImpl extends ServiceImpl<LendItemReturnMapper, LendItemReturn> implements LendItemReturnService {

    @Resource
    private UserBindService userBindService;
    @Resource
    private LendItemMapper lendItemMapper;
    @Resource
    private LendMapper lendMapper;
    @Resource
    private LendReturnMapper lendReturnMapper;
    
    @Override
    public List<LendItemReturn> selectByLendId(Long lendId, Long userId) {
        return baseMapper.selectList(
                new LambdaQueryWrapper<LendItemReturn>()
                        .eq(LendItemReturn::getLendId, lendId)
                        .eq(LendItemReturn::getInvestUserId, userId)
                        .orderByAsc(LendItemReturn::getCurrentPeriod));
    }

    /**
     * 添加还款明细
     * @param lendReturnId
     */
    @Override
    public List<Map<String, Object>> addReturnDetail(Long lendReturnId) {

        //Fetch return record
        LendReturn lendReturn = lendReturnMapper.selectById(lendReturnId);
        //Fetch trade info
        Lend lend = lendMapper.selectById(lendReturn.getLendId());

        List<LendItemReturn> lendItemReturnList = this.selectLendItemReturnList(lendReturnId);
        List<Map<String, Object>> lendItemReturnDetailList = new ArrayList<>();
        for(LendItemReturn lendItemReturn : lendItemReturnList) {
            LendItem lendItem = lendItemMapper.selectById(lendItemReturn.getLendItemId());
            String bindCode = userBindService.getBindCodeByUserId(lendItem.getInvestUserId());

            Map<String, Object> map = new HashMap<>();
            map.put("agentProjectCode", lend.getLendNo());
            map.put("voteBillNo", lendItem.getLendItemNo());
            map.put("toBindCode", bindCode);
            //Total return amount
            map.put("transitAmt", lendItemReturn.getTotal());
            //principal
            map.put("baseAmt", lendItemReturn.getPrincipal());
            //interest
            map.put("benifitAmt", lendItemReturn.getInterest());
            map.put("feeAmt", new BigDecimal("0"));

            lendItemReturnDetailList.add(map);
        }
        return lendItemReturnDetailList;
    }

    @Override
    public List<LendItemReturn> selectLendItemReturnList(Long lendReturnId) {
        return baseMapper.selectList(
                new LambdaQueryWrapper<LendItemReturn>()
                .eq(LendItemReturn::getLendReturnId,lendReturnId));
    }
}
