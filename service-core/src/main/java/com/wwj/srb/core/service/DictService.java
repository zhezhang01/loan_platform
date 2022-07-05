package com.wwj.srb.core.service;

import com.wwj.srb.core.pojo.dto.ExcelDictDTO;
import com.wwj.srb.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
public interface DictService extends IService<Dict> {

    /**
     * Get the Excel file uploaded from front end,sane it into the database
     * @param inputStream
     */
    void importData(InputStream inputStream);

    /**
     * Read thr data dict from database and generate corresponding Excel file
     * @return
     */
    List<ExcelDictDTO> listDictData();

    /**
     * Get child node data list according to the parent id
     * @param parentId
     * @return
     */
    List<Dict> listByParentId(Long parentId);

    List<Dict> findByDictCode(String dictCode);

    String getNameByParentDictCodeAndValue(String dictCode,Integer value);
}
