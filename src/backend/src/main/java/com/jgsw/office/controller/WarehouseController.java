package com.jgsw.office.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jgsw.office.entity.Warehouse;
import com.jgsw.office.service.WarehouseService;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/material/warehouse")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @GetMapping
    public Result<IPage<Warehouse>> getWarehouseList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String warehouseName) {
        return warehouseService.getWarehouseList(page, size, warehouseName);
    }

    @GetMapping("/{id}")
    public Result<Warehouse> getWarehouseById(@PathVariable Long id) {
        return warehouseService.getWarehouseById(id);
    }

    @PostMapping
    public Result<Warehouse> createWarehouse(@RequestBody Warehouse warehouse) {
        return warehouseService.createWarehouse(warehouse);
    }

    @PutMapping("/{id}")
    public Result<Warehouse> updateWarehouse(@PathVariable Long id, @RequestBody Warehouse warehouse) {
        warehouse.setId(id);
        return warehouseService.updateWarehouse(warehouse);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteWarehouse(@PathVariable Long id) {
        return warehouseService.deleteWarehouse(id);
    }

    @GetMapping("/all")
    public Result<List<Warehouse>> getAllWarehouses() {
        return warehouseService.getAllWarehouses();
    }
}
