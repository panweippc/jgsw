package com.jgsw.office.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgsw.office.entity.SysDictItem;
import com.jgsw.office.entity.SysDictType;
import com.jgsw.office.mapper.SysDictItemMapper;
import com.jgsw.office.mapper.SysDictTypeMapper;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DictService {

    @Autowired
    private SysDictTypeMapper sysDictTypeMapper;

    @Autowired
    private SysDictItemMapper sysDictItemMapper;

    public Result<IPage<SysDictType>> getDictTypeList(int page, int size, String dictTypeName) {
        Page<SysDictType> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<SysDictType> queryWrapper = new LambdaQueryWrapper<>();
        if (dictTypeName != null && !dictTypeName.isEmpty()) {
            queryWrapper.like(SysDictType::getDictTypeName, dictTypeName);
        }
        queryWrapper.eq(SysDictType::getStatus, 1);
        queryWrapper.orderByAsc(SysDictType::getSortOrder);
        IPage<SysDictType> typePage = sysDictTypeMapper.selectPage(pageParam, queryWrapper);
        return Result.success(typePage);
    }

    public Result<SysDictType> getDictTypeById(Long id) {
        SysDictType type = sysDictTypeMapper.selectById(id);
        if (type == null) {
            return Result.error(404, "字典类型不存在");
        }
        return Result.success(type);
    }

    @Transactional
    public Result<SysDictType> createDictType(SysDictType type) {
        LambdaQueryWrapper<SysDictType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictType::getDictTypeCode, type.getDictTypeCode());
        if (sysDictTypeMapper.selectOne(queryWrapper) != null) {
            return Result.error(400, "字典类型编码已存在");
        }

        type.setStatus(1);
        sysDictTypeMapper.insert(type);
        return Result.success("创建成功", type);
    }

    @Transactional
    public Result<SysDictType> updateDictType(SysDictType type) {
        SysDictType existing = sysDictTypeMapper.selectById(type.getId());
        if (existing == null) {
            return Result.error(404, "字典类型不存在");
        }

        sysDictTypeMapper.updateById(type);
        return Result.success("更新成功", type);
    }

    @Transactional
    public Result<Void> deleteDictType(Long id) {
        SysDictType type = sysDictTypeMapper.selectById(id);
        if (type == null) {
            return Result.error(404, "字典类型不存在");
        }

        sysDictItemMapper.delete(new LambdaQueryWrapper<SysDictItem>().eq(SysDictItem::getDictTypeId, id));
        sysDictTypeMapper.deleteById(id);
        return Result.success("删除成功");
    }

    public Result<List<SysDictItem>> getDictItems(Long typeId) {
        List<SysDictItem> items = sysDictItemMapper.selectList(
                new LambdaQueryWrapper<SysDictItem>()
                        .eq(SysDictItem::getDictTypeId, typeId)
                        .eq(SysDictItem::getStatus, 1)
                        .orderByAsc(SysDictItem::getSortOrder)
        );
        return Result.success(items);
    }

    @Transactional
    public Result<SysDictItem> createDictItem(SysDictItem item) {
        item.setStatus(1);
        sysDictItemMapper.insert(item);
        return Result.success("创建成功", item);
    }

    @Transactional
    public Result<SysDictItem> updateDictItem(SysDictItem item) {
        SysDictItem existing = sysDictItemMapper.selectById(item.getId());
        if (existing == null) {
            return Result.error(404, "字典项不存在");
        }

        sysDictItemMapper.updateById(item);
        return Result.success("更新成功", item);
    }

    @Transactional
    public Result<Void> deleteDictItem(Long id) {
        SysDictItem item = sysDictItemMapper.selectById(id);
        if (item == null) {
            return Result.error(404, "字典项不存在");
        }

        sysDictItemMapper.deleteById(id);
        return Result.success("删除成功");
    }
}
