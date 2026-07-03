package com.jgsw.office.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgsw.office.entity.Vehicle;
import com.jgsw.office.mapper.VehicleMapper;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehicleMapper vehicleMapper;

    public Result<IPage<Vehicle>> getVehicleList(int page, int size, String plateNumber, String vehicleType) {
        Page<Vehicle> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Vehicle> queryWrapper = new LambdaQueryWrapper<>();
        if (plateNumber != null && !plateNumber.isEmpty()) {
            queryWrapper.like(Vehicle::getPlateNumber, plateNumber);
        }
        if (vehicleType != null && !vehicleType.isEmpty()) {
            queryWrapper.eq(Vehicle::getVehicleType, vehicleType);
        }
        queryWrapper.orderByAsc(Vehicle::getCreateTime);
        IPage<Vehicle> vehiclePage = vehicleMapper.selectPage(pageParam, queryWrapper);
        return Result.success(vehiclePage);
    }

    public Result<Vehicle> getVehicleById(Long id) {
        Vehicle vehicle = vehicleMapper.selectById(id);
        if (vehicle == null) {
            return Result.error(404, "车辆不存在");
        }
        return Result.success(vehicle);
    }

    @Transactional
    public Result<Vehicle> createVehicle(Vehicle vehicle) {
        LambdaQueryWrapper<Vehicle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Vehicle::getPlateNumber, vehicle.getPlateNumber());
        if (vehicleMapper.selectOne(queryWrapper) != null) {
            return Result.error(400, "车牌号已存在");
        }

        if (vehicle.getStatus() == null) {
            vehicle.setStatus(1);
        }
        vehicleMapper.insert(vehicle);
        return Result.success("创建成功", vehicle);
    }

    @Transactional
    public Result<Vehicle> updateVehicle(Vehicle vehicle) {
        Vehicle existing = vehicleMapper.selectById(vehicle.getId());
        if (existing == null) {
            return Result.error(404, "车辆不存在");
        }

        if (vehicle.getPlateNumber() != null) {
            existing.setPlateNumber(vehicle.getPlateNumber());
        }
        if (vehicle.getVehicleType() != null) {
            existing.setVehicleType(vehicle.getVehicleType());
        }
        if (vehicle.getBrand() != null) {
            existing.setBrand(vehicle.getBrand());
        }
        if (vehicle.getModel() != null) {
            existing.setModel(vehicle.getModel());
        }
        if (vehicle.getColor() != null) {
            existing.setColor(vehicle.getColor());
        }
        if (vehicle.getSeatCount() != null) {
            existing.setSeatCount(vehicle.getSeatCount());
        }
        if (vehicle.getPurchaseDate() != null) {
            existing.setPurchaseDate(vehicle.getPurchaseDate());
        }
        if (vehicle.getMileage() != null) {
            existing.setMileage(vehicle.getMileage());
        }
        if (vehicle.getFuelType() != null) {
            existing.setFuelType(vehicle.getFuelType());
        }
        if (vehicle.getEngineNo() != null) {
            existing.setEngineNo(vehicle.getEngineNo());
        }
        if (vehicle.getVin() != null) {
            existing.setVin(vehicle.getVin());
        }
        if (vehicle.getInsuranceExpire() != null) {
            existing.setInsuranceExpire(vehicle.getInsuranceExpire());
        }
        if (vehicle.getInspectionExpire() != null) {
            existing.setInspectionExpire(vehicle.getInspectionExpire());
        }
        if (vehicle.getStatus() != null) {
            existing.setStatus(vehicle.getStatus());
        }
        if (vehicle.getRemark() != null) {
            existing.setRemark(vehicle.getRemark());
        }

        vehicleMapper.updateById(existing);
        return Result.success("更新成功", existing);
    }

    @Transactional
    public Result<Void> deleteVehicle(Long id) {
        Vehicle vehicle = vehicleMapper.selectById(id);
        if (vehicle == null) {
            return Result.error(404, "车辆不存在");
        }

        vehicle.setStatus(0);
        vehicleMapper.updateById(vehicle);
        return Result.success("删除成功");
    }

    public Result<List<Vehicle>> getAvailableVehicles() {
        List<Vehicle> vehicles = vehicleMapper.selectList(
                new LambdaQueryWrapper<Vehicle>().eq(Vehicle::getStatus, 1)
        );
        return Result.success(vehicles);
    }

    public Result<List<Vehicle>> getAllVehicles() {
        List<Vehicle> vehicles = vehicleMapper.selectList(null);
        return Result.success(vehicles);
    }

    public Result<String> exportVehicles(String plateNumber, String vehicleType) {
        LambdaQueryWrapper<Vehicle> queryWrapper = new LambdaQueryWrapper<>();
        if (plateNumber != null && !plateNumber.isEmpty()) {
            queryWrapper.like(Vehicle::getPlateNumber, plateNumber);
        }
        if (vehicleType != null && !vehicleType.isEmpty()) {
            queryWrapper.eq(Vehicle::getVehicleType, vehicleType);
        }
        queryWrapper.orderByAsc(Vehicle::getCreateTime);

        List<Vehicle> vehicles = vehicleMapper.selectList(queryWrapper);

        StringBuilder csv = new StringBuilder();
        csv.append("车牌号,车辆类型,品牌,车型,颜色,座位数,购置日期,当前里程,燃油类型,发动机号,车架号,保险到期,年检到期,状态,备注\n");

        for (Vehicle vehicle : vehicles) {
            csv.append(vehicle.getPlateNumber() != null ? vehicle.getPlateNumber() : "").append(",");
            csv.append(vehicle.getVehicleType() != null ? vehicle.getVehicleType() : "").append(",");
            csv.append(vehicle.getBrand() != null ? vehicle.getBrand() : "").append(",");
            csv.append(vehicle.getModel() != null ? vehicle.getModel() : "").append(",");
            csv.append(vehicle.getColor() != null ? vehicle.getColor() : "").append(",");
            csv.append(vehicle.getSeatCount() != null ? vehicle.getSeatCount() : "").append(",");
            csv.append(vehicle.getPurchaseDate() != null ? vehicle.getPurchaseDate().toString() : "").append(",");
            csv.append(vehicle.getMileage() != null ? vehicle.getMileage() : "").append(",");
            csv.append(vehicle.getFuelType() != null ? vehicle.getFuelType() : "").append(",");
            csv.append(vehicle.getEngineNo() != null ? vehicle.getEngineNo() : "").append(",");
            csv.append(vehicle.getVin() != null ? vehicle.getVin() : "").append(",");
            csv.append(vehicle.getInsuranceExpire() != null ? vehicle.getInsuranceExpire().toString() : "").append(",");
            csv.append(vehicle.getInspectionExpire() != null ? vehicle.getInspectionExpire().toString() : "").append(",");
            csv.append(getStatusLabel(vehicle.getStatus())).append(",");
            csv.append(vehicle.getRemark() != null ? vehicle.getRemark().replaceAll(",", "，") : "").append("\n");
        }

        return Result.success(csv.toString());
    }

    private String getStatusLabel(Integer status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 0: return "停用";
            case 1: return "在用";
            case 2: return "维修中";
            case 3: return "已报废";
            default: return "未知";
        }
    }
}