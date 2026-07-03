package com.jgsw.office.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgsw.office.entity.MaterialOutbound;
import com.jgsw.office.entity.MaterialStock;
import com.jgsw.office.mapper.MaterialOutboundMapper;
import com.jgsw.office.mapper.MaterialStockMapper;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OutboundService {

    @Autowired
    private MaterialOutboundMapper materialOutboundMapper;

    @Autowired
    private MaterialStockMapper materialStockMapper;

    public Result<IPage<MaterialOutbound>> getOutboundList(int page, int size, String outboundNo, Long materialId) {
        Page<MaterialOutbound> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<MaterialOutbound> queryWrapper = new LambdaQueryWrapper<>();
        if (outboundNo != null && !outboundNo.isEmpty()) {
            queryWrapper.like(MaterialOutbound::getOutboundNo, outboundNo);
        }
        if (materialId != null) {
            queryWrapper.eq(MaterialOutbound::getMaterialId, materialId);
        }
        queryWrapper.eq(MaterialOutbound::getStatus, 1);
        queryWrapper.orderByDesc(MaterialOutbound::getCreateTime);
        IPage<MaterialOutbound> outboundPage = materialOutboundMapper.selectPage(pageParam, queryWrapper);
        return Result.success(outboundPage);
    }

    public Result<MaterialOutbound> getOutboundById(Long id) {
        MaterialOutbound outbound = materialOutboundMapper.selectById(id);
        if (outbound == null) {
            return Result.error(404, "出库单不存在");
        }
        return Result.success(outbound);
    }

    @Transactional
    public Result<MaterialOutbound> createOutbound(MaterialOutbound outbound) {
        LambdaQueryWrapper<MaterialStock> stockQuery = new LambdaQueryWrapper<>();
        stockQuery.eq(MaterialStock::getMaterialId, outbound.getMaterialId());
        stockQuery.eq(MaterialStock::getWarehouseId, outbound.getWarehouseId());
        MaterialStock stock = materialStockMapper.selectOne(stockQuery);

        if (stock == null || stock.getAvailableQuantity() < outbound.getQuantity()) {
            return Result.error(400, "库存不足");
        }

        outbound.setStatus(1);
        materialOutboundMapper.insert(outbound);

        stock.setQuantity(stock.getQuantity() - outbound.getQuantity());
        stock.setAvailableQuantity(stock.getAvailableQuantity() - outbound.getQuantity());
        stock.setLastOutboundTime(LocalDateTime.now());
        materialStockMapper.updateById(stock);

        return Result.success("创建成功", outbound);
    }

    @Transactional
    public Result<MaterialOutbound> updateOutbound(MaterialOutbound outbound) {
        MaterialOutbound existing = materialOutboundMapper.selectById(outbound.getId());
        if (existing == null) {
            return Result.error(404, "出库单不存在");
        }

        materialOutboundMapper.updateById(outbound);
        return Result.success("更新成功", outbound);
    }

    @Transactional
    public Result<Void> deleteOutbound(Long id) {
        MaterialOutbound outbound = materialOutboundMapper.selectById(id);
        if (outbound == null) {
            return Result.error(404, "出库单不存在");
        }

        outbound.setStatus(0);
        materialOutboundMapper.updateById(outbound);
        return Result.success("删除成功");
    }
}
