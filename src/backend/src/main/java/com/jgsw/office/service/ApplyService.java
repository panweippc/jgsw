package com.jgsw.office.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgsw.office.entity.MaterialApply;
import com.jgsw.office.entity.MaterialStock;
import com.jgsw.office.mapper.MaterialApplyMapper;
import com.jgsw.office.mapper.MaterialStockMapper;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ApplyService {

    @Autowired
    private MaterialApplyMapper materialApplyMapper;

    @Autowired
    private MaterialStockMapper materialStockMapper;

    public Result<IPage<MaterialApply>> getApplyList(int page, int size, String applyNo, Integer status) {
        Page<MaterialApply> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<MaterialApply> queryWrapper = new LambdaQueryWrapper<>();
        if (applyNo != null && !applyNo.isEmpty()) {
            queryWrapper.like(MaterialApply::getApplyNo, applyNo);
        }
        if (status != null) {
            queryWrapper.eq(MaterialApply::getStatus, status);
        }
        queryWrapper.orderByDesc(MaterialApply::getCreateTime);
        IPage<MaterialApply> applyPage = materialApplyMapper.selectPage(pageParam, queryWrapper);
        return Result.success(applyPage);
    }

    public Result<MaterialApply> getApplyById(Long id) {
        MaterialApply apply = materialApplyMapper.selectById(id);
        if (apply == null) {
            return Result.error(404, "领用申请不存在");
        }
        return Result.success(apply);
    }

    @Transactional
    public Result<MaterialApply> createApply(MaterialApply apply) {
        apply.setStatus(0);
        materialApplyMapper.insert(apply);

        LambdaQueryWrapper<MaterialStock> stockQuery = new LambdaQueryWrapper<>();
        stockQuery.eq(MaterialStock::getMaterialId, apply.getMaterialId());
        stockQuery.eq(MaterialStock::getWarehouseId, apply.getWarehouseId());
        MaterialStock stock = materialStockMapper.selectOne(stockQuery);

        if (stock != null) {
            stock.setAvailableQuantity(stock.getAvailableQuantity() - apply.getQuantity());
            stock.setLockedQuantity(stock.getLockedQuantity() + apply.getQuantity());
            materialStockMapper.updateById(stock);
        }

        return Result.success("创建成功", apply);
    }

    @Transactional
    public Result<MaterialApply> approveApply(Long id, Integer status) {
        MaterialApply apply = materialApplyMapper.selectById(id);
        if (apply == null) {
            return Result.error(404, "领用申请不存在");
        }

        if (apply.getStatus() != 0) {
            return Result.error(400, "该申请已处理");
        }

        apply.setStatus(status);
        apply.setApproveTime(LocalDateTime.now());
        materialApplyMapper.updateById(apply);

        if (status == 1) {
            LambdaQueryWrapper<MaterialStock> stockQuery = new LambdaQueryWrapper<>();
            stockQuery.eq(MaterialStock::getMaterialId, apply.getMaterialId());
            stockQuery.eq(MaterialStock::getWarehouseId, apply.getWarehouseId());
            MaterialStock stock = materialStockMapper.selectOne(stockQuery);

            if (stock != null) {
                stock.setQuantity(stock.getQuantity() - apply.getQuantity());
                stock.setLockedQuantity(stock.getLockedQuantity() - apply.getQuantity());
                stock.setLastOutboundTime(LocalDateTime.now());
                materialStockMapper.updateById(stock);
            }
        } else {
            LambdaQueryWrapper<MaterialStock> stockQuery = new LambdaQueryWrapper<>();
            stockQuery.eq(MaterialStock::getMaterialId, apply.getMaterialId());
            stockQuery.eq(MaterialStock::getWarehouseId, apply.getWarehouseId());
            MaterialStock stock = materialStockMapper.selectOne(stockQuery);

            if (stock != null) {
                stock.setAvailableQuantity(stock.getAvailableQuantity() + apply.getQuantity());
                stock.setLockedQuantity(stock.getLockedQuantity() - apply.getQuantity());
                materialStockMapper.updateById(stock);
            }
        }

        return Result.success(status == 1 ? "审批通过" : "审批拒绝", apply);
    }

    @Transactional
    public Result<Void> deleteApply(Long id) {
        MaterialApply apply = materialApplyMapper.selectById(id);
        if (apply == null) {
            return Result.error(404, "领用申请不存在");
        }

        materialApplyMapper.deleteById(id);
        return Result.success("删除成功");
    }
}
