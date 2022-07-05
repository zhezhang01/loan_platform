package com.wwj.srb.core.controller.admin;


import com.alibaba.excel.EasyExcel;
import com.wwj.common.result.R;
import com.wwj.common.result.ResponseEnum;
import com.wwj.common.result.exception.BusinessException;
import com.wwj.srb.core.pojo.dto.ExcelDictDTO;
import com.wwj.srb.core.pojo.entity.Dict;
import com.wwj.srb.core.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Api(tags = "Data Dict management")
@RestController
@RequestMapping("/admin/core/dict")
@Slf4j
//@CrossOrigin
public class AdminDictController {

    @Autowired
    private DictService dictService;

    @ApiOperation("import Excel data")
    @PostMapping("/import")
    public R batchImport(
            @ApiParam(value = "Excel data dict file", required = true)
            @RequestParam("file") MultipartFile file) {
        try {
            dictService.importData(file.getInputStream());
            return R.ok().message("Success!");
        } catch (Exception e) {
            throw new BusinessException(ResponseEnum.UPLOAD_ERROR, e);
        }
    }

    @ApiOperation("export Excel data")
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("mydict", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), ExcelDictDTO.class).sheet("Data_dict").doWrite(dictService.listDictData());

    }

    @ApiOperation("Get data list according to the parent node id")
    @GetMapping("/listByParentId/{parentId}")
    public R listByParentId(
            @ApiParam(value = "parent node id", required = true)
            @PathVariable("parentId") Long parentId) {
        List<Dict> dictList = dictService.listByParentId(parentId);
        return R.ok().data("list",dictList);
    }
}

