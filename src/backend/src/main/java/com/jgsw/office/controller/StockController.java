package com.jgsw.office.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jgsw.office.entity.MaterialStock;
import com.jgsw.office.service.StockService;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/material/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping
    public Result<IPage<MaterialStock>> getStockList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long materialId,
            @RequestParam(required = false) Long warehouseId) {
        return stockService.getStockList(page, size, materialId, warehouseId);
    }

    @GetMapping("/{id}")
    public Result<MaterialStock> getStockById(@PathVariable Long id) {
        return stockService.getStockById(id);
    }

    @GetMapping("/material/{materialId}")
    public Result<List<MaterialStock>> getStockByMaterialId(@PathVariable Long materialId) {
        return stockService.getStockByMaterialId(materialId);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public Result<List<MaterialStock>> getStockByWarehouseId(@PathVariable Long warehouseId) {
        return stockService.getStockByWarehouseId(warehouseId);
    }
}
