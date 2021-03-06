package com.wwj.srb.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wwj.srb.core.pojo.entity.BorrowerAttach;
import com.wwj.srb.core.pojo.vo.BorrowerAttachVO;

import java.util.List;

/**
 * <p>
 * 借款人上传资源表 服务类
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
public interface BorrowerAttachService extends IService<BorrowerAttach> {

    List<BorrowerAttachVO> selectBorrowerAttachVOList(Long borrowerId);
}
