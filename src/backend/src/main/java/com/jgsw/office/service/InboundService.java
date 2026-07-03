package com.jgsw.office.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgsw.office.entity.MaterialInbound;
import com.jgsw.office.entity.MaterialStock;
import com.jgsw.office.mapper.MaterialInboundMapper;
import com.jgsw.office.mapper.MaterialStockMapper;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class InboundService {

    @Autowired
    private MaterialInboundMapper materialInboundMapper;

    @Autowired
    private MaterialStockMapper materialStockMapper;

    public Result<IPage<MaterialInbound>> getInboundList(int page, int size, String inboundNo, Long materialId) {
        Page<MaterialInbound> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<MaterialInbound> queryWrapper = new LambdaQueryWrapper<>();
        if (inboundNo != null && !inboundNo.isEmpty()) {
            queryWrapper.like(MaterialInbound::getInboundNo, inboundNo);
        }
        if (materialId != null) {
            queryWrapper.eq(MaterialInbound::getMaterialId, materialId);
        }
        queryWrapper.eq(MaterialInbound::getStatus, 1);
        queryWrapper.orderByDesc(MaterialInbound::getCreateTime);
        IPage<MaterialInbound> inboundPage = materialInboundMapper.selectPage(pageParam, queryWrapper);
        return Result.success(inboundPage);
    }

    public Result<MaterialInbound> getInboundById(Long id) {
        MaterialInbound inbound = materialInboundMapper.selectById(id);
        if (inbound == null) {
            return Result.error(404, "入库单不存在");
        }
        return Result.success(inbound);
    }

    @Transactional
    public Result<MaterialInbound> createInbound(MaterialInbound inbound) {
        inbound.setStatus(1);
        materialInboundMapper.insert(inbound);

        updateStock(inbound.getMaterialId(), inbound.getWarehouseId(), inbound.getQuantity());

        return Result.success("创建成功", inbound);
    }

    @Transactional
    public Result<MaterialInbound> updateInbound(MaterialInbound inbound) {
        MaterialInbound existing = materialInboundMapper.selectById(inbound.getId());
        if (existing == null) {
            return Result.error(404, "入库单不存在");
        }

        materialInboundMapper.updateById(inbound);
        return Result.success("更新成功", inbound);
    }

    @Transactional
    public Result<Void> deleteInbound(Long id) {
        MaterialInbound inbound = materialInboundMapper.selectById(id);
        if (inbound == null) {
            return Result.error(404, "入库单不存在");
        }

        inbound.setStatus(0);
        materialInboundMapper.updateById(inbound);
        return Result.success("删除成功");
    }

    private void updateStock(Long materialId, Long warehouseId, Integer quantity) {
        LambdaQueryWrapper<MaterialStock> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MaterialStock::getMaterialId, materialId);
        queryWrapper.eq(MaterialStock::getWarehouseId, warehouseId);
        MaterialStock stock = materialStockMapper.selectOne(queryWrapper);

        if (stock == null) {
            stock = new MaterialStock();
            stock.setMaterialId(materialId);
            stock.setWarehouseId(warehouseId);
            stock.setQuantity(quantity);
            stock.setAvailableQuantity(quantity);
            stock.setLockedQuantity(0);
            stock.setLastInboundTime(LocalDateTime.now());
            materialStockMapper.insert(stock);
        } else {
            stock.setQuantity(stock.getQuantity() + quantity);
            stock.setAvailableQuantity(stock.getAvailableQuantity() + quantity);
            stock.setLastInboundTime(LocalDateTime.now());
            materialStockMapper.updateById(stock);
        }
    }
}
