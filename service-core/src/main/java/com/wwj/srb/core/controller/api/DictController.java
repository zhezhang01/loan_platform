package com.wwj.srb.core.controller.api;

import com.wwj.common.result.R;
import com.wwj.srb.core.pojo.entity.Dict;
import com.wwj.srb.core.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "Data Dict")
@RestController
@RequestMapping("/api/core/dict")
@Slf4j
public class DictController {

    @Autowired
    private DictService dictService;

    @ApiOperation("get child node info according to dictCode")
    @GetMapping("/findByDictCode/{dictCode}")
    public R findByDictCode(
            @ApiParam(value = "node number", required = true)
            @PathVariable String dictCode) {
        List<Dict> dictList = dictService.findByDictCode(dictCode);
        return R.ok().data("dictList", dictList);
    }
}
