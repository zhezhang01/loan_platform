package com.wwj.srb.core.controller.admin;

import com.wwj.common.result.R;
import com.wwj.srb.core.pojo.entity.BorrowInfo;
import com.wwj.srb.core.pojo.vo.BorrowInfoApprovalVO;
import com.wwj.srb.core.service.BorrowInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "Load management")
@RestController
@RequestMapping("/admin/core/borrowInfo")
@Slf4j
public class AdminBorrowInfoController {

    @Autowired
    private BorrowInfoService borrowInfoService;

    @ApiOperation("load info list")
    @GetMapping("/list")
    public R list() {
        List<BorrowInfo> borrowInfoList = borrowInfoService.selectList();
        return R.ok().data("list", borrowInfoList);
    }

    @ApiOperation("loan info details")
    @GetMapping("/show/{id}")
    public R show(
            @ApiParam(value = "load_info_id", required = true)
            @PathVariable Long id) {
        Map<String, Object> borrowInfoDetail = borrowInfoService.getBorrowInfoDetail(id);
        return R.ok().data("borrowInfoDetail", borrowInfoDetail);
    }

    @ApiOperation("Review Loan info")
    @PostMapping("/approval")
    public R approval(@RequestBody BorrowInfoApprovalVO borrowInfoApprovalVO) {
        borrowInfoService.approval(borrowInfoApprovalVO);
        return R.ok().message("Load Approved");
    }
}
