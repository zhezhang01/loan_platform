package com.wwj.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwj.srb.core.enums.BorrowerStatusEnum;
import com.wwj.srb.core.enums.IntegralEnum;
import com.wwj.srb.core.mapper.BorrowerAttachMapper;
import com.wwj.srb.core.mapper.BorrowerMapper;
import com.wwj.srb.core.mapper.UserInfoMapper;
import com.wwj.srb.core.mapper.UserIntegralMapper;
import com.wwj.srb.core.pojo.entity.Borrower;
import com.wwj.srb.core.pojo.entity.BorrowerAttach;
import com.wwj.srb.core.pojo.entity.UserInfo;
import com.wwj.srb.core.pojo.entity.UserIntegral;
import com.wwj.srb.core.pojo.vo.BorrowerApprovalVO;
import com.wwj.srb.core.pojo.vo.BorrowerAttachVO;
import com.wwj.srb.core.pojo.vo.BorrowerDetailVO;
import com.wwj.srb.core.pojo.vo.BorrowerVO;
import com.wwj.srb.core.service.BorrowerAttachService;
import com.wwj.srb.core.service.BorrowerService;
import com.wwj.srb.core.service.DictService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 借款人 服务实现类
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Service
public class BorrowerServiceImpl extends ServiceImpl<BorrowerMapper, Borrower> implements BorrowerService {

    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private BorrowerAttachMapper borrowerAttachMapper;
    @Autowired
    private DictService dictService;
    @Autowired
    private BorrowerAttachService borrowerAttachService;
    @Resource
    private UserIntegralMapper userIntegralMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveBorrowerVOByUserId(BorrowerVO borrowerVO, Long userId) {
        // save user's basic info
        UserInfo userInfo = userInfoMapper.selectById(userId);

        // keep borrower's info
        Borrower borrower = new Borrower();
        BeanUtils.copyProperties(borrowerVO, borrower);

        borrower.setUserId(userId);
        borrower.setName(userInfo.getName());
        borrower.setIdCard(userInfo.getIdCard());
        borrower.setMobile(userInfo.getMobile());
        // Set the status into reviewing
        borrower.setStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());
        baseMapper.insert(borrower);

        // Save attachment
        List<BorrowerAttach> borrowerAttachList = borrowerVO.getBorrowerAttachList();
        borrowerAttachList.forEach(borrowerAttach -> {
            borrowerAttach.setBorrowerId(borrower.getId());
            borrowerAttachMapper.insert(borrowerAttach);
        });

        // update the certification status of user_info form
        userInfo.setBorrowAuthStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public Integer getStatusByUserId(Long userId) {
        List<Object> statusList = baseMapper.selectObjs(
                new LambdaQueryWrapper<Borrower>()
                        .select(Borrower::getStatus)
                        .eq(Borrower::getUserId, userId));
        if (statusList.size() == 0) {
            return BorrowerStatusEnum.NO_AUTH.getStatus();
        }
        return (Integer) statusList.get(0);
    }

    @Override
    public IPage<Borrower> listPage(Page<Borrower> pageParam, String keyword) {
        // Check if the query criteria exists
        if (StringUtils.isBlank(keyword)) {
            return baseMapper.selectPage(pageParam, null);
        }
        return baseMapper.selectPage(pageParam,
                new LambdaQueryWrapper<Borrower>()
                        .like(Borrower::getName, keyword)
                        .or().like(Borrower::getIdCard, keyword)
                        .or().like(Borrower::getMobile, keyword)
                        .orderByDesc(Borrower::getId));
    }

    @Override
    public BorrowerDetailVO getBorrowerDetailVOById(Long id) {
        // Fetch the borrower info
        Borrower borrower = baseMapper.selectById(id);
        // fill the basic info of the borrower
        BorrowerDetailVO borrowerDetailVO = new BorrowerDetailVO();
        BeanUtils.copyProperties(borrower, borrowerDetailVO);
        // Marriage
        borrowerDetailVO.setMarry(borrower.getMarry() ? "Yes" : "No");
        // Gender
        borrowerDetailVO.setSex(borrower.getSex() == 1 ? "Male" : "Female");

        borrowerDetailVO.setEducation(dictService.getNameByParentDictCodeAndValue("education", borrower.getEducation()));
        borrowerDetailVO.setIndustry(dictService.getNameByParentDictCodeAndValue("industry", borrower.getIndustry()));
        borrowerDetailVO.setIncome(dictService.getNameByParentDictCodeAndValue("income", borrower.getIncome()));
        borrowerDetailVO.setReturnSource(dictService.getNameByParentDictCodeAndValue("returnSource", borrower.getReturnSource()));
        borrowerDetailVO.setContactsRelation(dictService.getNameByParentDictCodeAndValue("relation", borrower.getContactsRelation()));
        // certification status
        Integer status = borrower.getStatus();
        borrowerDetailVO.setStatus(BorrowerStatusEnum.getMsgByStatus(status));
        // Attachment
        List<BorrowerAttachVO> borrowerAttachVOList = borrowerAttachService.selectBorrowerAttachVOList(id);
        borrowerDetailVO.setBorrowerAttachVOList(borrowerAttachVOList);
        return borrowerDetailVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void approval(BorrowerApprovalVO borrowerApprovalVO) {
        // get borrow info id
        Long borrowerId = borrowerApprovalVO.getBorrowerId();

        Borrower borrower = baseMapper.selectById(borrowerId);
        // set reviewing status
        borrower.setStatus(borrowerApprovalVO.getStatus());
        baseMapper.updateById(borrower);

        // get membership id
        Long userId = borrower.getUserId();
        // get membership objection
        UserInfo userInfo = userInfoMapper.selectById(userId);
        // get initial integral
        Integer integral = userInfo.getIntegral();

        // set membership basic integral
        UserIntegral userIntegral = new UserIntegral();
        userIntegral.setUserId(userId);
        userIntegral.setIntegral(borrowerApprovalVO.getInfoIntegral());
        userIntegral.setContent("Borrower info");
        userIntegralMapper.insert(userIntegral);
        int currentIntegral = integral + borrowerApprovalVO.getInfoIntegral();

        // set attachment integral（ID）
        if (borrowerApprovalVO.getIsIdCardOk()) {
            userIntegral = new UserIntegral();
            userIntegral.setUserId(userId);
            userIntegral.setIntegral(IntegralEnum.BORROWER_IDCARD.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_IDCARD.getMsg());
            userIntegralMapper.insert(userIntegral);
            currentIntegral += IntegralEnum.BORROWER_IDCARD.getIntegral();
        }
        // set attachment integral （house）
        if (borrowerApprovalVO.getIsHouseOk()) {
            userIntegral = new UserIntegral();
            userIntegral.setUserId(userId);
            userIntegral.setIntegral(IntegralEnum.BORROWER_HOUSE.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_HOUSE.getMsg());
            userIntegralMapper.insert(userIntegral);
            currentIntegral += IntegralEnum.BORROWER_HOUSE.getIntegral();
        }
        // set attachment integral （vehicle）
        if (borrowerApprovalVO.getIsCarOk()) {
            userIntegral = new UserIntegral();
            userIntegral.setUserId(userId);
            userIntegral.setIntegral(IntegralEnum.BORROWER_CAR.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_CAR.getMsg());
            userIntegralMapper.insert(userIntegral);
            currentIntegral += IntegralEnum.BORROWER_CAR.getIntegral();
        }
        // Set overall integral
        userInfo.setIntegral(currentIntegral);
        // update reviewing status
        userInfo.setBorrowAuthStatus(borrowerApprovalVO.getStatus());
        // update userinfo
        userInfoMapper.updateById(userInfo);
    }
}
