package com.jgsw.office.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jgsw.office.entity.MaterialInbound;
import com.jgsw.office.service.InboundService;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/material/inbound")
public class InboundController {

    @Autowired
    private InboundService inboundService;

    @GetMapping
    public Result<IPage<MaterialInbound>> getInboundList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String inboundNo,
            @RequestParam(required = false) Long materialId) {
        return inboundService.getInboundList(page, size, inboundNo, materialId);
    }

    @GetMapping("/{id}")
    public Result<MaterialInbound> getInboundById(@PathVariable Long id) {
        return inboundService.getInboundById(id);
    }

    @PostMapping
    public Result<MaterialInbound> createInbound(@RequestBody MaterialInbound inbound) {
        return inboundService.createInbound(inbound);
    }

    @PutMapping("/{id}")
    public Result<MaterialInbound> updateInbound(@PathVariable Long id, @RequestBody MaterialInbound inbound) {
        inbound.setId(id);
        return inboundService.updateInbound(inbound);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteInbound(@PathVariable Long id) {
        return inboundService.deleteInbound(id);
    }
}
