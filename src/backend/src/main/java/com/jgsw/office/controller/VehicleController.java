package com.jgsw.office.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jgsw.office.entity.Vehicle;
import com.jgsw.office.service.VehicleService;
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
@RequestMapping("/api/dispatch/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping
    public Result<IPage<Vehicle>> getVehicleList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String plateNumber,
            @RequestParam(required = false) String vehicleType) {
        return vehicleService.getVehicleList(page, size, plateNumber, vehicleType);
    }

    @GetMapping("/{id}")
    public Result<Vehicle> getVehicleById(@PathVariable Long id) {
        return vehicleService.getVehicleById(id);
    }

    @PostMapping
    public Result<Vehicle> createVehicle(@RequestBody Vehicle vehicle) {
        return vehicleService.createVehicle(vehicle);
    }

    @PutMapping("/{id}")
    public Result<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        vehicle.setId(id);
        return vehicleService.updateVehicle(vehicle);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteVehicle(@PathVariable Long id) {
        return vehicleService.deleteVehicle(id);
    }

    @GetMapping("/available")
    public Result<List<Vehicle>> getAvailableVehicles() {
        return vehicleService.getAvailableVehicles();
    }

    @GetMapping("/all")
    public Result<List<Vehicle>> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportVehicles(
            @RequestParam(required = false) String plateNumber,
            @RequestParam(required = false) String vehicleType) {
        Result<String> result = vehicleService.exportVehicles(plateNumber, vehicleType);
        if (result.getCode() != 200) {
            return ResponseEntity.badRequest().body(null);
        }

        byte[] csvBytes = result.getData().getBytes(StandardCharsets.UTF_8);
        String filename = "车辆信息_" + LocalDate.now().toString() + ".csv";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
        headers.setContentDispositionFormData("attachment", new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
        headers.setContentLength(csvBytes.length);

        return ResponseEntity.ok().headers(headers).body(csvBytes);
    }
}