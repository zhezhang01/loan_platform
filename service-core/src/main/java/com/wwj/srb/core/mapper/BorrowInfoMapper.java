package com.wwj.srb.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wwj.srb.core.pojo.entity.BorrowInfo;

import java.util.List;

/**
 * <p>
 * 借款信息表 Mapper 接口
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
public interface BorrowInfoMapper extends BaseMapper<BorrowInfo> {

    List<BorrowInfo> selectBorrowInfoList();
}
