package com.jgsw.office.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jgsw.office.entity.DispatchOrder;
import com.jgsw.office.service.DispatchOrderService;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/dispatch/order")
public class DispatchOrderController {

    @Autowired
    private DispatchOrderService dispatchOrderService;

    @GetMapping
    public Result<IPage<DispatchOrder>> getOrderList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) LocalDate useDate) {
        return dispatchOrderService.getOrderList(page, size, orderNo, status, useDate);
    }

    @GetMapping("/{id}")
    public Result<DispatchOrder> getOrderById(@PathVariable Long id) {
        return dispatchOrderService.getOrderById(id);
    }

    @PostMapping
    public Result<DispatchOrder> createOrder(@RequestBody DispatchOrder order) {
        return dispatchOrderService.createOrder(order);
    }

    @PutMapping("/{id}")
    public Result<DispatchOrder> updateOrder(@PathVariable Long id, @RequestBody DispatchOrder order) {
        order.setId(id);
        return dispatchOrderService.updateOrder(order);
    }

    @PutMapping("/{id}/approve")
    public Result<DispatchOrder> approveOrder(
            @PathVariable Long id,
            @RequestParam Integer status,
            @RequestParam(required = false) String approveRemark) {
        return dispatchOrderService.approveOrder(id, status, approveRemark);
    }

    @PutMapping("/{id}/complete")
    public Result<DispatchOrder> completeOrder(
            @PathVariable Long id,
            @RequestParam(required = false) BigDecimal actualMileage) {
        return dispatchOrderService.completeOrder(id, actualMileage);
    }

    @PutMapping("/{id}/cancel")
    public Result<DispatchOrder> cancelOrder(@PathVariable Long id) {
        return dispatchOrderService.cancelOrder(id);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteOrder(@PathVariable Long id) {
        return dispatchOrderService.deleteOrder(id);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportOrders(@RequestParam(required = false) Integer status) {
        Result<String> result = dispatchOrderService.exportOrders(status);
        if (result.getCode() != 200) {
            return ResponseEntity.badRequest().body(null);
        }

        byte[] csvBytes = result.getData().getBytes(StandardCharsets.UTF_8);
        String filename = "派车单_" + LocalDate.now().toString() + ".csv";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
        headers.setContentDispositionFormData("attachment", new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
        headers.setContentLength(csvBytes.length);

        return ResponseEntity.ok().headers(headers).body(csvBytes);
    }
}