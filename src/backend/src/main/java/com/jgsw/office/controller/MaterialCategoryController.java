package com.jgsw.office.controller;

import com.jgsw.office.entity.MaterialCategory;
import com.jgsw.office.service.MaterialCategoryService;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/material/category")
public class MaterialCategoryController {

    @Autowired
    private MaterialCategoryService materialCategoryService;

    @GetMapping
    public Result<List<MaterialCategory>> getCategoryTree() {
        return materialCategoryService.getCategoryTree();
    }

    @GetMapping("/{id}")
    public Result<MaterialCategory> getCategoryById(@PathVariable Long id) {
        return materialCategoryService.getCategoryById(id);
    }

    @PostMapping
    public Result<MaterialCategory> createCategory(@RequestBody MaterialCategory category) {
        return materialCategoryService.createCategory(category);
    }

    @PutMapping("/{id}")
    public Result<MaterialCategory> updateCategory(@PathVariable Long id, @RequestBody MaterialCategory category) {
        category.setId(id);
        return materialCategoryService.updateCategory(category);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        return materialCategoryService.deleteCategory(id);
    }
}
