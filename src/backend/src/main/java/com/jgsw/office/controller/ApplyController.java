package com.jgsw.office.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jgsw.office.entity.MaterialApply;
import com.jgsw.office.service.ApplyService;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/material/apply")
public class ApplyController {

    @Autowired
    private ApplyService applyService;

    @GetMapping
    public Result<IPage<MaterialApply>> getApplyList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String applyNo,
            @RequestParam(required = false) Integer status) {
        return applyService.getApplyList(page, size, applyNo, status);
    }

    @GetMapping("/{id}")
    public Result<MaterialApply> getApplyById(@PathVariable Long id) {
        return applyService.getApplyById(id);
    }

    @PostMapping
    public Result<MaterialApply> createApply(@RequestBody MaterialApply apply) {
        return applyService.createApply(apply);
    }

    @PutMapping("/{id}/approve")
    public Result<MaterialApply> approveApply(@PathVariable Long id, @RequestParam Integer status) {
        return applyService.approveApply(id, status);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteApply(@PathVariable Long id) {
        return applyService.deleteApply(id);
    }
}
