package com.jgsw.office.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgsw.office.entity.Driver;
import com.jgsw.office.mapper.DriverMapper;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DriverService {

    @Autowired
    private DriverMapper driverMapper;

    public Result<IPage<Driver>> getDriverList(int page, int size, String driverName, String department) {
        Page<Driver> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Driver> queryWrapper = new LambdaQueryWrapper<>();
        if (driverName != null && !driverName.isEmpty()) {
            queryWrapper.like(Driver::getDriverName, driverName);
        }
        if (department != null && !department.isEmpty()) {
            queryWrapper.eq(Driver::getDepartment, department);
        }
        queryWrapper.orderByAsc(Driver::getCreateTime);
        IPage<Driver> driverPage = driverMapper.selectPage(pageParam, queryWrapper);
        return Result.success(driverPage);
    }

    public Result<Driver> getDriverById(Long id) {
        Driver driver = driverMapper.selectById(id);
        if (driver == null) {
            return Result.error(404, "驾驶员不存在");
        }
        return Result.success(driver);
    }

    @Transactional
    public Result<Driver> createDriver(Driver driver) {
        LambdaQueryWrapper<Driver> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Driver::getDriverLicense, driver.getDriverLicense());
        if (driverMapper.selectOne(queryWrapper) != null) {
            return Result.error(400, "驾驶证号已存在");
        }

        if (driver.getStatus() == null) {
            driver.setStatus(1);
        }
        driverMapper.insert(driver);
        return Result.success("创建成功", driver);
    }

    @Transactional
    public Result<Driver> updateDriver(Driver driver) {
        Driver existing = driverMapper.selectById(driver.getId());
        if (existing == null) {
            return Result.error(404, "驾驶员不存在");
        }

        if (driver.getDriverName() != null) {
            existing.setDriverName(driver.getDriverName());
        }
        if (driver.getPhone() != null) {
            existing.setPhone(driver.getPhone());
        }
        if (driver.getDriverLicense() != null) {
            existing.setDriverLicense(driver.getDriverLicense());
        }
        if (driver.getLicenseType() != null) {
            existing.setLicenseType(driver.getLicenseType());
        }
        if (driver.getLicenseExpire() != null) {
            existing.setLicenseExpire(driver.getLicenseExpire());
        }
        if (driver.getHireDate() != null) {
            existing.setHireDate(driver.getHireDate());
        }
        if (driver.getDepartment() != null) {
            existing.setDepartment(driver.getDepartment());
        }
        if (driver.getStatus() != null) {
            existing.setStatus(driver.getStatus());
        }
        if (driver.getRemark() != null) {
            existing.setRemark(driver.getRemark());
        }

        driverMapper.updateById(existing);
        return Result.success("更新成功", existing);
    }

    @Transactional
    public Result<Void> deleteDriver(Long id) {
        Driver driver = driverMapper.selectById(id);
        if (driver == null) {
            return Result.error(404, "驾驶员不存在");
        }

        driver.setStatus(0);
        driverMapper.updateById(driver);
        return Result.success("删除成功");
    }

    public Result<List<Driver>> getAvailableDrivers() {
        List<Driver> drivers = driverMapper.selectList(
                new LambdaQueryWrapper<Driver>().eq(Driver::getStatus, 1)
        );
        return Result.success(drivers);
    }

    public Result<List<Driver>> getAllDrivers() {
        List<Driver> drivers = driverMapper.selectList(null);
        return Result.success(drivers);
    }

    public Result<String> exportDrivers(String driverName, String department) {
        LambdaQueryWrapper<Driver> queryWrapper = new LambdaQueryWrapper<>();
        if (driverName != null && !driverName.isEmpty()) {
            queryWrapper.like(Driver::getDriverName, driverName);
        }
        if (department != null && !department.isEmpty()) {
            queryWrapper.eq(Driver::getDepartment, department);
        }
        queryWrapper.orderByAsc(Driver::getCreateTime);

        List<Driver> drivers = driverMapper.selectList(queryWrapper);

        StringBuilder csv = new StringBuilder();
        csv.append("姓名,手机号,驾驶证号,准驾车型,驾驶证到期,入职日期,所属部门,状态,备注\n");

        for (Driver driver : drivers) {
            csv.append(driver.getDriverName() != null ? driver.getDriverName() : "").append(",");
            csv.append(driver.getPhone() != null ? driver.getPhone() : "").append(",");
            csv.append(driver.getDriverLicense() != null ? driver.getDriverLicense() : "").append(",");
            csv.append(driver.getLicenseType() != null ? driver.getLicenseType() : "").append(",");
            csv.append(driver.getLicenseExpire() != null ? driver.getLicenseExpire().toString() : "").append(",");
            csv.append(driver.getHireDate() != null ? driver.getHireDate().toString() : "").append(",");
            csv.append(driver.getDepartment() != null ? driver.getDepartment() : "").append(",");
            csv.append(getStatusLabel(driver.getStatus())).append(",");
            csv.append(driver.getRemark() != null ? driver.getRemark().replaceAll(",", "，") : "").append("\n");
        }

        return Result.success(csv.toString());
    }

    private String getStatusLabel(Integer status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 0: return "停用";
            case 1: return "在职";
            case 2: return "离职";
            default: return "未知";
        }
    }
}