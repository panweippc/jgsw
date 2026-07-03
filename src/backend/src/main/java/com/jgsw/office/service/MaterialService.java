package com.jgsw.office.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgsw.office.entity.Material;
import com.jgsw.office.mapper.MaterialMapper;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaterialService {

    @Autowired
    private MaterialMapper materialMapper;

    public Result<IPage<Material>> getMaterialList(int page, int size, String materialName, Long categoryId) {
        Page<Material> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Material> queryWrapper = new LambdaQueryWrapper<>();
        if (materialName != null && !materialName.isEmpty()) {
            queryWrapper.like(Material::getMaterialName, materialName);
        }
        if (categoryId != null) {
            queryWrapper.eq(Material::getCategoryId, categoryId);
        }
        queryWrapper.eq(Material::getStatus, 1);
        queryWrapper.orderByDesc(Material::getCreateTime);
        IPage<Material> materialPage = materialMapper.selectPage(pageParam, queryWrapper);
        return Result.success(materialPage);
    }

    public Result<Material> getMaterialById(Long id) {
        Material material = materialMapper.selectById(id);
        if (material == null) {
            return Result.error(404, "物资不存在");
        }
        return Result.success(material);
    }

    @Transactional
    public Result<Material> createMaterial(Material material) {
        LambdaQueryWrapper<Material> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Material::getMaterialCode, material.getMaterialCode());
        if (materialMapper.selectOne(queryWrapper) != null) {
            return Result.error(400, "物资编码已存在");
        }

        material.setStatus(1);
        materialMapper.insert(material);
        return Result.success("创建成功", material);
    }

    @Transactional
    public Result<Material> updateMaterial(Material material) {
        Material existing = materialMapper.selectById(material.getId());
        if (existing == null) {
            return Result.error(404, "物资不存在");
        }

        materialMapper.updateById(material);
        return Result.success("更新成功", material);
    }

    @Transactional
    public Result<Void> deleteMaterial(Long id) {
        Material material = materialMapper.selectById(id);
        if (material == null) {
            return Result.error(404, "物资不存在");
        }

        material.setStatus(0);
        materialMapper.updateById(material);
        return Result.success("删除成功");
    }
}
