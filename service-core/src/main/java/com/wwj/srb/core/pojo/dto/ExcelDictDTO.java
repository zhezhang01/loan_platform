package com.wwj.srb.core.pojo.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Data dict
 */
@Data
public class ExcelDictDTO {

    @ExcelProperty("id")
    private Long id;

    @ExcelProperty("Parent_id")
    private Long parentId;

    @ExcelProperty("Name")
    private String name;

    @ExcelProperty("Value")
    private Integer value;

    @ExcelProperty("Code")
    private String dictCode;
}
