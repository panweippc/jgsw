package com.jgsw.office.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgsw.office.entity.MaterialStock;
import com.jgsw.office.mapper.MaterialStockMapper;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    @Autowired
    private MaterialStockMapper materialStockMapper;

    public Result<IPage<MaterialStock>> getStockList(int page, int size, Long materialId, Long warehouseId) {
        Page<MaterialStock> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<MaterialStock> queryWrapper = new LambdaQueryWrapper<>();
        if (materialId != null) {
            queryWrapper.eq(MaterialStock::getMaterialId, materialId);
        }
        if (warehouseId != null) {
            queryWrapper.eq(MaterialStock::getWarehouseId, warehouseId);
        }
        queryWrapper.orderByDesc(MaterialStock::getUpdateTime);
        IPage<MaterialStock> stockPage = materialStockMapper.selectPage(pageParam, queryWrapper);
        return Result.success(stockPage);
    }

    public Result<MaterialStock> getStockById(Long id) {
        MaterialStock stock = materialStockMapper.selectById(id);
        if (stock == null) {
            return Result.error(404, "库存记录不存在");
        }
        return Result.success(stock);
    }

    public Result<List<MaterialStock>> getStockByMaterialId(Long materialId) {
        List<MaterialStock> stocks = materialStockMapper.selectList(
                new LambdaQueryWrapper<MaterialStock>().eq(MaterialStock::getMaterialId, materialId)
        );
        return Result.success(stocks);
    }

    public Result<List<MaterialStock>> getStockByWarehouseId(Long warehouseId) {
        List<MaterialStock> stocks = materialStockMapper.selectList(
                new LambdaQueryWrapper<MaterialStock>().eq(MaterialStock::getWarehouseId, warehouseId)
        );
        return Result.success(stocks);
    }
}
