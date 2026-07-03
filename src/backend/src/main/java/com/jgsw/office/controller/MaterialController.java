package com.jgsw.office.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jgsw.office.entity.Material;
import com.jgsw.office.service.MaterialService;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/material/info")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @GetMapping
    public Result<IPage<Material>> getMaterialList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String materialName,
            @RequestParam(required = false) Long categoryId) {
        return materialService.getMaterialList(page, size, materialName, categoryId);
    }

    @GetMapping("/{id}")
    public Result<Material> getMaterialById(@PathVariable Long id) {
        return materialService.getMaterialById(id);
    }

    @PostMapping
    public Result<Material> createMaterial(@RequestBody Material material) {
        return materialService.createMaterial(material);
    }

    @PutMapping("/{id}")
    public Result<Material> updateMaterial(@PathVariable Long id, @RequestBody Material material) {
        material.setId(id);
        return materialService.updateMaterial(material);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteMaterial(@PathVariable Long id) {
        return materialService.deleteMaterial(id);
    }
}
