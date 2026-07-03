package com.jgsw.office.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jgsw.office.entity.MaterialCategory;
import com.jgsw.office.mapper.MaterialCategoryMapper;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialCategoryService {

    @Autowired
    private MaterialCategoryMapper materialCategoryMapper;

    public Result<List<MaterialCategory>> getCategoryTree() {
        List<MaterialCategory> categories = materialCategoryMapper.selectList(
                new LambdaQueryWrapper<MaterialCategory>()
                        .eq(MaterialCategory::getStatus, 1)
                        .orderByAsc(MaterialCategory::getSortOrder)
        );
        return Result.success(buildTree(categories));
    }

    public Result<MaterialCategory> getCategoryById(Long id) {
        MaterialCategory category = materialCategoryMapper.selectById(id);
        if (category == null) {
            return Result.error(404, "分类不存在");
        }
        return Result.success(category);
    }

    @Transactional
    public Result<MaterialCategory> createCategory(MaterialCategory category) {
        LambdaQueryWrapper<MaterialCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MaterialCategory::getCategoryCode, category.getCategoryCode());
        if (materialCategoryMapper.selectOne(queryWrapper) != null) {
            return Result.error(400, "分类编码已存在");
        }

        category.setStatus(1);
        materialCategoryMapper.insert(category);
        return Result.success("创建成功", category);
    }

    @Transactional
    public Result<MaterialCategory> updateCategory(MaterialCategory category) {
        MaterialCategory existing = materialCategoryMapper.selectById(category.getId());
        if (existing == null) {
            return Result.error(404, "分类不存在");
        }

        materialCategoryMapper.updateById(category);
        return Result.success("更新成功", category);
    }

    @Transactional
    public Result<Void> deleteCategory(Long id) {
        MaterialCategory category = materialCategoryMapper.selectById(id);
        if (category == null) {
            return Result.error(404, "分类不存在");
        }

        materialCategoryMapper.delete(new LambdaQueryWrapper<MaterialCategory>().eq(MaterialCategory::getParentId, id));
        materialCategoryMapper.deleteById(id);
        return Result.success("删除成功");
    }

    private List<MaterialCategory> buildTree(List<MaterialCategory> categories) {
        return categories.stream()
                .filter(c -> c.getParentId() == 0)
                .peek(c -> c.setChildren(
                        categories.stream()
                                .filter(child -> child.getParentId().equals(c.getId()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
}
