package com.wwj.srb.core.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wwj.common.result.R;
import com.wwj.srb.core.pojo.entity.Borrower;
import com.wwj.srb.core.pojo.vo.BorrowerApprovalVO;
import com.wwj.srb.core.pojo.vo.BorrowerDetailVO;
import com.wwj.srb.core.service.BorrowerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Borrower Management")
@RestController
@RequestMapping("/admin/core/borrower")
@Slf4j
public class AdminBorrowerController {

    @Autowired
    private BorrowerService borrowerService;

    @ApiOperation("Get the list of borrower")
    @GetMapping("/list/{page}/{limit}")
    public R listPage(
            @ApiParam(value = "Current page", required = true)
            @PathVariable Long page,
            @ApiParam(value = "Records per page", required = true)
            @PathVariable Long limit,
            @ApiParam(value = "Search for key words", required = false)
            @RequestParam String keyword) {
        Page<Borrower> pageParam = new Page<>(page, limit);
        IPage<Borrower> pageModel = borrowerService.listPage(pageParam, keyword);
        return R.ok().data("pageModel", pageModel);
    }

    @ApiOperation("Get the basic info of borrower")
    @GetMapping("/show/{id}")
    public R show(
            @ApiParam(value = "Borrower id", required = true)
            @PathVariable Long id) {
        BorrowerDetailVO borrowerDetailVO = borrowerService.getBorrowerDetailVOById(id);
        return R.ok().data("borrowerDetailVO", borrowerDetailVO);
    }

    @ApiOperation("Examine the amount of load")
    @PostMapping("/approval")
    public R approval(@RequestBody BorrowerApprovalVO borrowerApprovalVO) {
        borrowerService.approval(borrowerApprovalVO);
        return R.ok().message("Examination finished");
    }
}
