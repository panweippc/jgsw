package com.jgsw.office.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgsw.office.entity.Warehouse;
import com.jgsw.office.mapper.WarehouseMapper;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseMapper warehouseMapper;

    public Result<IPage<Warehouse>> getWarehouseList(int page, int size, String warehouseName) {
        Page<Warehouse> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Warehouse> queryWrapper = new LambdaQueryWrapper<>();
        if (warehouseName != null && !warehouseName.isEmpty()) {
            queryWrapper.like(Warehouse::getWarehouseName, warehouseName);
        }
        queryWrapper.eq(Warehouse::getStatus, 1);
        queryWrapper.orderByAsc(Warehouse::getSortOrder);
        IPage<Warehouse> warehousePage = warehouseMapper.selectPage(pageParam, queryWrapper);
        return Result.success(warehousePage);
    }

    public Result<Warehouse> getWarehouseById(Long id) {
        Warehouse warehouse = warehouseMapper.selectById(id);
        if (warehouse == null) {
            return Result.error(404, "仓库不存在");
        }
        return Result.success(warehouse);
    }

    @Transactional
    public Result<Warehouse> createWarehouse(Warehouse warehouse) {
        LambdaQueryWrapper<Warehouse> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Warehouse::getWarehouseCode, warehouse.getWarehouseCode());
        if (warehouseMapper.selectOne(queryWrapper) != null) {
            return Result.error(400, "仓库编码已存在");
        }

        warehouse.setStatus(1);
        warehouseMapper.insert(warehouse);
        return Result.success("创建成功", warehouse);
    }

    @Transactional
    public Result<Warehouse> updateWarehouse(Warehouse warehouse) {
        Warehouse existing = warehouseMapper.selectById(warehouse.getId());
        if (existing == null) {
            return Result.error(404, "仓库不存在");
        }

        warehouseMapper.updateById(warehouse);
        return Result.success("更新成功", warehouse);
    }

    @Transactional
    public Result<Void> deleteWarehouse(Long id) {
        Warehouse warehouse = warehouseMapper.selectById(id);
        if (warehouse == null) {
            return Result.error(404, "仓库不存在");
        }

        warehouse.setStatus(0);
        warehouseMapper.updateById(warehouse);
        return Result.success("删除成功");
    }

    public Result<List<Warehouse>> getAllWarehouses() {
        List<Warehouse> warehouses = warehouseMapper.selectList(
                new LambdaQueryWrapper<Warehouse>().eq(Warehouse::getStatus, 1)
        );
        return Result.success(warehouses);
    }
}
