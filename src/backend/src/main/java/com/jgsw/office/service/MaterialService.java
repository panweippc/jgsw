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

        if (material.getMaterialCode() != null) {
            existing.setMaterialCode(material.getMaterialCode());
        }
        if (material.getMaterialName() != null) {
            existing.setMaterialName(material.getMaterialName());
        }
        if (material.getCategoryId() != null) {
            existing.setCategoryId(material.getCategoryId());
        }
        if (material.getSpec() != null) {
            existing.setSpec(material.getSpec());
        }
        if (material.getUnit() != null) {
            existing.setUnit(material.getUnit());
        }
        if (material.getBrand() != null) {
            existing.setBrand(material.getBrand());
        }
        if (material.getOrigin() != null) {
            existing.setOrigin(material.getOrigin());
        }
        if (material.getPurchasePrice() != null) {
            existing.setPurchasePrice(material.getPurchasePrice());
        }
        if (material.getRetailPrice() != null) {
            existing.setRetailPrice(material.getRetailPrice());
        }
        if (material.getMinStock() != null) {
            existing.setMinStock(material.getMinStock());
        }
        if (material.getMaxStock() != null) {
            existing.setMaxStock(material.getMaxStock());
        }
        if (material.getDescription() != null) {
            existing.setDescription(material.getDescription());
        }
        if (material.getStatus() != null) {
            existing.setStatus(material.getStatus());
        }

        materialMapper.updateById(existing);
        return Result.success("更新成功", existing);
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
