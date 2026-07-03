package com.jgsw.office.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgsw.office.entity.DispatchOrder;
import com.jgsw.office.entity.Driver;
import com.jgsw.office.entity.Vehicle;
import com.jgsw.office.mapper.DispatchOrderMapper;
import com.jgsw.office.mapper.DriverMapper;
import com.jgsw.office.mapper.VehicleMapper;
import com.jgsw.office.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DispatchOrderService {

    @Autowired
    private DispatchOrderMapper dispatchOrderMapper;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private DriverMapper driverMapper;

    public Result<IPage<DispatchOrder>> getOrderList(int page, int size, String orderNo, Integer status, LocalDate useDate) {
        Page<DispatchOrder> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<DispatchOrder> queryWrapper = new LambdaQueryWrapper<>();
        if (orderNo != null && !orderNo.isEmpty()) {
            queryWrapper.like(DispatchOrder::getOrderNo, orderNo);
        }
        if (status != null) {
            queryWrapper.eq(DispatchOrder::getStatus, status);
        }
        if (useDate != null) {
            queryWrapper.eq(DispatchOrder::getUseDate, useDate);
        }
        queryWrapper.orderByAsc(DispatchOrder::getCreateTime);
        IPage<DispatchOrder> orderPage = dispatchOrderMapper.selectPage(pageParam, queryWrapper);

        for (DispatchOrder order : orderPage.getRecords()) {
            fillOrderInfo(order);
        }

        return Result.success(orderPage);
    }

    public Result<DispatchOrder> getOrderById(Long id) {
        DispatchOrder order = dispatchOrderMapper.selectById(id);
        if (order == null) {
            return Result.error(404, "派车单不存在");
        }
        fillOrderInfo(order);
        return Result.success(order);
    }

    @Transactional
    public Result<DispatchOrder> createOrder(DispatchOrder order) {
        String orderNo = generateOrderNo();
        order.setOrderNo(orderNo);
        order.setStatus(0);
        dispatchOrderMapper.insert(order);
        fillOrderInfo(order);
        return Result.success("创建成功", order);
    }

    @Transactional
    public Result<DispatchOrder> updateOrder(DispatchOrder order) {
        DispatchOrder existing = dispatchOrderMapper.selectById(order.getId());
        if (existing == null) {
            return Result.error(404, "派车单不存在");
        }

        if (existing.getStatus() != 0) {
            return Result.error(400, "该派车单已处理，无法修改");
        }

        if (order.getVehicleId() != null) {
            existing.setVehicleId(order.getVehicleId());
        }
        if (order.getDriverId() != null) {
            existing.setDriverId(order.getDriverId());
        }
        if (order.getStartPlace() != null) {
            existing.setStartPlace(order.getStartPlace());
        }
        if (order.getEndPlace() != null) {
            existing.setEndPlace(order.getEndPlace());
        }
        if (order.getUseDate() != null) {
            existing.setUseDate(order.getUseDate());
        }
        if (order.getStartTime() != null) {
            existing.setStartTime(order.getStartTime());
        }
        if (order.getEndTime() != null) {
            existing.setEndTime(order.getEndTime());
        }
        if (order.getPurpose() != null) {
            existing.setPurpose(order.getPurpose());
        }
        if (order.getPassengerCount() != null) {
            existing.setPassengerCount(order.getPassengerCount());
        }
        if (order.getRemark() != null) {
            existing.setRemark(order.getRemark());
        }

        dispatchOrderMapper.updateById(existing);
        fillOrderInfo(existing);
        return Result.success("更新成功", existing);
    }

    @Transactional
    public Result<DispatchOrder> approveOrder(Long id, Integer status, String approveRemark) {
        DispatchOrder order = dispatchOrderMapper.selectById(id);
        if (order == null) {
            return Result.error(404, "派车单不存在");
        }

        if (order.getStatus() != 0) {
            return Result.error(400, "该派车单已处理");
        }

        order.setStatus(status);
        order.setApproveTime(LocalDateTime.now());
        order.setApproveRemark(approveRemark);
        dispatchOrderMapper.updateById(order);
        fillOrderInfo(order);

        if (status == 1) {
            order.setStatus(3);
            dispatchOrderMapper.updateById(order);
            return Result.success("审批通过，已派车", order);
        } else {
            return Result.success("审批拒绝", order);
        }
    }

    @Transactional
    public Result<DispatchOrder> completeOrder(Long id, BigDecimal actualMileage) {
        DispatchOrder order = dispatchOrderMapper.selectById(id);
        if (order == null) {
            return Result.error(404, "派车单不存在");
        }

        if (order.getStatus() != 3) {
            return Result.error(400, "该派车单未派车，无法完成");
        }

        order.setStatus(4);
        order.setActualMileage(actualMileage);
        dispatchOrderMapper.updateById(order);
        fillOrderInfo(order);
        return Result.success("派车单已完成", order);
    }

    @Transactional
    public Result<DispatchOrder> cancelOrder(Long id) {
        DispatchOrder order = dispatchOrderMapper.selectById(id);
        if (order == null) {
            return Result.error(404, "派车单不存在");
        }

        if (order.getStatus() == 4) {
            return Result.error(400, "该派车单已完成，无法取消");
        }

        order.setStatus(5);
        dispatchOrderMapper.updateById(order);
        fillOrderInfo(order);
        return Result.success("派车单已取消", order);
    }

    @Transactional
    public Result<Void> deleteOrder(Long id) {
        DispatchOrder order = dispatchOrderMapper.selectById(id);
        if (order == null) {
            return Result.error(404, "派车单不存在");
        }

        dispatchOrderMapper.deleteById(id);
        return Result.success("删除成功");
    }

    public Result<String> exportOrders(Integer status) {
        LambdaQueryWrapper<DispatchOrder> queryWrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            queryWrapper.eq(DispatchOrder::getStatus, status);
        }
        queryWrapper.orderByAsc(DispatchOrder::getCreateTime);

        List<DispatchOrder> orders = dispatchOrderMapper.selectList(queryWrapper);
        for (DispatchOrder order : orders) {
            fillOrderInfo(order);
        }

        StringBuilder csv = new StringBuilder();
        csv.append("派车单号,车牌号,驾驶员,申请人,申请部门,出发地点,目的地,用车日期,出发时间,预计返回时间,用车事由,乘车人数,状态,审批人,审批时间,实际里程,备注\n");

        for (DispatchOrder order : orders) {
            csv.append(order.getOrderNo()).append(",");
            csv.append(order.getPlateNumber() != null ? order.getPlateNumber() : "").append(",");
            csv.append(order.getDriverName() != null ? order.getDriverName() : "").append(",");
            csv.append(order.getApplicantName() != null ? order.getApplicantName() : "").append(",");
            csv.append(order.getApplyDept() != null ? order.getApplyDept() : "").append(",");
            csv.append(order.getStartPlace() != null ? order.getStartPlace() : "").append(",");
            csv.append(order.getEndPlace() != null ? order.getEndPlace() : "").append(",");
            csv.append(order.getUseDate() != null ? order.getUseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "").append(",");
            csv.append(order.getStartTime() != null ? order.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) : "").append(",");
            csv.append(order.getEndTime() != null ? order.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) : "").append(",");
            csv.append(order.getPurpose() != null ? order.getPurpose() : "").append(",");
            csv.append(order.getPassengerCount() != null ? order.getPassengerCount() : "").append(",");
            csv.append(getStatusLabel(order.getStatus())).append(",");
            csv.append(order.getApproveName() != null ? order.getApproveName() : "").append(",");
            csv.append(order.getApproveTime() != null ? order.getApproveTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "").append(",");
            csv.append(order.getActualMileage() != null ? order.getActualMileage() : "").append(",");
            csv.append(order.getRemark() != null ? order.getRemark().replaceAll(",", "，") : "").append("\n");
        }

        return Result.success(csv.toString());
    }

    private String generateOrderNo() {
        String prefix = "PC";
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        LambdaQueryWrapper<DispatchOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(DispatchOrder::getOrderNo, prefix + "-" + date);
        queryWrapper.orderByAsc(DispatchOrder::getOrderNo);
        DispatchOrder lastOrder = dispatchOrderMapper.selectOne(queryWrapper);

        int seq = 1;
        if (lastOrder != null) {
            String lastNo = lastOrder.getOrderNo();
            String seqStr = lastNo.substring(lastNo.lastIndexOf("-") + 1);
            seq = Integer.parseInt(seqStr) + 1;
        }

        return String.format("%s-%s-%03d", prefix, date, seq);
    }

    private void fillOrderInfo(DispatchOrder order) {
        if (order.getVehicleId() != null) {
            Vehicle vehicle = vehicleMapper.selectById(order.getVehicleId());
            if (vehicle != null) {
                order.setPlateNumber(vehicle.getPlateNumber());
            }
        }
        if (order.getDriverId() != null) {
            Driver driver = driverMapper.selectById(order.getDriverId());
            if (driver != null) {
                order.setDriverName(driver.getDriverName());
            }
        }
    }

    private String getStatusLabel(Integer status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 0: return "待审批";
            case 1: return "已批准";
            case 2: return "已拒绝";
            case 3: return "已派车";
            case 4: return "已完成";
            case 5: return "已取消";
            default: return "未知";
        }
    }
}