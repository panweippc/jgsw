package com.jgsw.office.controller;

import com.jgsw.office.entity.SysDept;
import com.jgsw.office.service.DeptService;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @GetMapping
    public Result<List<SysDept>> getDeptTree() {
        return deptService.getDeptTree();
    }

    @GetMapping("/{id}")
    public Result<SysDept> getDeptById(@PathVariable Long id) {
        return deptService.getDeptById(id);
    }

    @PostMapping
    public Result<SysDept> createDept(@RequestBody SysDept dept) {
        return deptService.createDept(dept);
    }

    @PutMapping("/{id}")
    public Result<SysDept> updateDept(@PathVariable Long id, @RequestBody SysDept dept) {
        dept.setId(id);
        return deptService.updateDept(dept);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteDept(@PathVariable Long id) {
        return deptService.deleteDept(id);
    }
}
