package com.jgsw.office.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jgsw.office.entity.MaterialOutbound;
import com.jgsw.office.service.OutboundService;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/material/outbound")
public class OutboundController {

    @Autowired
    private OutboundService outboundService;

    @GetMapping
    public Result<IPage<MaterialOutbound>> getOutboundList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String outboundNo,
            @RequestParam(required = false) Long materialId) {
        return outboundService.getOutboundList(page, size, outboundNo, materialId);
    }

    @GetMapping("/{id}")
    public Result<MaterialOutbound> getOutboundById(@PathVariable Long id) {
        return outboundService.getOutboundById(id);
    }

    @PostMapping
    public Result<MaterialOutbound> createOutbound(@RequestBody MaterialOutbound outbound) {
        return outboundService.createOutbound(outbound);
    }

    @PutMapping("/{id}")
    public Result<MaterialOutbound> updateOutbound(@PathVariable Long id, @RequestBody MaterialOutbound outbound) {
        outbound.setId(id);
        return outboundService.updateOutbound(outbound);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteOutbound(@PathVariable Long id) {
        return outboundService.deleteOutbound(id);
    }
}
