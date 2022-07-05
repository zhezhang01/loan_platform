package com.wwj.srb.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwj.srb.core.listener.ExcelDictDTOListener;
import com.wwj.srb.core.mapper.DictMapper;
import com.wwj.srb.core.pojo.dto.ExcelDictDTO;
import com.wwj.srb.core.pojo.entity.Dict;
import com.wwj.srb.core.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author zhezhang
 * @since 2022-06-17
 */
@Service
@Slf4j
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Resource
    private DictMapper dictMapper;
    @Resource
    private RedisTemplate redisTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importData(InputStream inputStream) {
        // Read Excel form
        EasyExcel.read(inputStream, ExcelDictDTO.class, new ExcelDictDTOListener(dictMapper)).sheet().doRead();
        log.info("import Excel successfully!");
    }

    @Override
    public List<ExcelDictDTO> listDictData() {
        List<Dict> dictList = baseMapper.selectList(null);
        // collect DictList as ExcelDictDTOList
        List<ExcelDictDTO> excelDictDTOList = new ArrayList<>();
        dictList.forEach(dict -> {
            ExcelDictDTO excelDictDTO = new ExcelDictDTO();
            // Copy attribute
            BeanUtils.copyProperties(dict, excelDictDTO);
            excelDictDTOList.add(excelDictDTO);
        });
        return excelDictDTOList;
    }

    @Override
    public List<Dict> listByParentId(Long parentId) {
        try {
            //Check if data list exist in redis
            List<Dict> dictList = (List<Dict>) redisTemplate.opsForValue().get("srb:core:dictList:" + parentId);
            if (dictList != null) {
                // if exists in Redis，return the data list from Redis
                log.info("get dict list from Redis");
                return dictList;
            }
        } catch (Exception e) {
            log.error("Redis running error:" + ExceptionUtils.getStackTrace(e));
        }
        // If not exists in Redis, make a query in database
        log.info("get data dict from database");
        List<Dict> list = baseMapper.selectList(
                new LambdaQueryWrapper<Dict>()
                        .eq(Dict::getParentId, parentId));
        List<Dict> dictList = list.stream().map(dict -> {
            boolean hasChildren = hasChildren(dict.getId());
            dict.setHasChildren(hasChildren);
            return dict;
        }).collect(Collectors.toList());

        try {
            // After querying the database，put the data list into Redis，expiration time is 5 mins
            log.info("save data list into Redis");
            redisTemplate.opsForValue().set("srb:core:dictList:" + parentId, dictList, 5, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("Redis running error:" + ExceptionUtils.getStackTrace(e));
        }
        // return data
        return dictList;
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        Dict dict = baseMapper.selectOne(
                new LambdaQueryWrapper<Dict>()
                        .eq(Dict::getDictCode, dictCode));
        return this.listByParentId(dict.getId());
    }

    @Override
    public String getNameByParentDictCodeAndValue(String dictCode, Integer value) {
        Dict parentDict = baseMapper.selectOne(
                new LambdaQueryWrapper<Dict>()
                        .eq(Dict::getDictCode, dictCode));
        if (parentDict == null) {
            return "";
        }
        Dict dict = baseMapper.selectOne(
                new LambdaQueryWrapper<Dict>()
                        .eq(Dict::getParentId, parentDict.getId())
                        .eq(Dict::getValue, value));
        if (dict == null) {
            return "";
        }
        return dict.getName();
    }

    /**
     * 判断当前id所在的节点下是否有子节点
     *
     * @param id
     * @return
     */
    private boolean hasChildren(Long id) {
        Integer count = dictMapper.selectCount(
                new LambdaQueryWrapper<Dict>()
                        .eq(Dict::getParentId, id));
        return count > 0;
    }
}
