package com.jgsw.office.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jgsw.office.entity.Driver;
import com.jgsw.office.service.DriverService;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dispatch/driver")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @GetMapping
    public Result<IPage<Driver>> getDriverList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String driverName,
            @RequestParam(required = false) String department) {
        return driverService.getDriverList(page, size, driverName, department);
    }

    @GetMapping("/{id}")
    public Result<Driver> getDriverById(@PathVariable Long id) {
        return driverService.getDriverById(id);
    }

    @PostMapping
    public Result<Driver> createDriver(@RequestBody Driver driver) {
        return driverService.createDriver(driver);
    }

    @PutMapping("/{id}")
    public Result<Driver> updateDriver(@PathVariable Long id, @RequestBody Driver driver) {
        driver.setId(id);
        return driverService.updateDriver(driver);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteDriver(@PathVariable Long id) {
        return driverService.deleteDriver(id);
    }

    @GetMapping("/available")
    public Result<List<Driver>> getAvailableDrivers() {
        return driverService.getAvailableDrivers();
    }

    @GetMapping("/all")
    public Result<List<Driver>> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportDrivers(
            @RequestParam(required = false) String driverName,
            @RequestParam(required = false) String department) {
        Result<String> result = driverService.exportDrivers(driverName, department);
        if (result.getCode() != 200) {
            return ResponseEntity.badRequest().body(null);
        }

        byte[] csvBytes = result.getData().getBytes(StandardCharsets.UTF_8);
        String filename = "驾驶员信息_" + LocalDate.now().toString() + ".csv";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
        headers.setContentDispositionFormData("attachment", new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
        headers.setContentLength(csvBytes.length);

        return ResponseEntity.ok().headers(headers).body(csvBytes);
    }
}