package com.wwj.srb.core.controller.admin;

import com.wwj.common.result.R;
import com.wwj.common.result.ResponseEnum;
import com.wwj.common.result.exception.Assert;
import com.wwj.srb.core.pojo.entity.IntegralGrade;
import com.wwj.srb.core.service.IntegralGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/core/integralGrade")
//@CrossOrigin
@Api(tags = "Credit management")
@Slf4j
public class AdminIntegralGradeController {

    @Autowired
    private IntegralGradeService integralGradeService;

    @ApiOperation("Credit level list")
    @GetMapping("/list")
    public R listAll() {
        List<IntegralGrade> list = integralGradeService.list();
        return R.ok().data("list", list).message("Success to get the list");
    }

    @ApiOperation(value = "Delete credit level according to the id", notes = "Delete data")
    @DeleteMapping("/remove/{id}")
    public R removeById(
            @ApiParam(value = "data ID", required = true)
            @PathVariable("id") Long id) {
        boolean result = integralGradeService.removeById(id);
        if (result) {
            return R.ok().message("Successfully Deleted");
        } else {
            return R.error().message("Failure to delete");
        }
    }

    @ApiOperation("Add new credit level")
    @PostMapping("/save")
    public R save(
            @ApiParam(value = "Credit level objection", required = true)
            @RequestBody IntegralGrade integralGrade) {

//        if (integralGrade.getBorrowAmount() == null) {
//            throw new BusinessException(ResponseEnum.BORROW_AMOUNT_NULL_ERROR);
//        }

        Assert.notNull(integralGrade.getBorrowAmount(), ResponseEnum.BORROW_AMOUNT_NULL_ERROR);

        boolean result = integralGradeService.save(integralGrade);
        if (result) {
            return R.ok().message("Success to save!");
        } else {
            return R.error().message("Failure to save!");
        }
    }

    @ApiOperation("Get od according to credit level")
    @GetMapping("/get/{id}")
    public R getById(
            @ApiParam(value = "data ID", required = true)
            @PathVariable("id") Long id) {
        IntegralGrade integralGrade = integralGradeService.getById(id);
        if (integralGrade != null) {
            return R.ok().data("record", integralGrade);
        } else {
            return R.error().message("Failure to fetch data");
        }
    }

    @ApiOperation("Update credit level")
    @PutMapping("/update")
    public R updateById(
            @ApiParam(value = "Credit level objection", required = true)
            @RequestBody IntegralGrade integralGrade) {
        boolean result = integralGradeService.updateById(integralGrade);
        if (result) {
            return R.ok().message("Success to update");
        } else {
            return R.error().message("Failure to update");
        }
    }
}
