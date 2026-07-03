package com.jgsw.office.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@TableName("dispatch_order")
public class DispatchOrder {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("order_no")
    private String orderNo;

    @TableField("vehicle_id")
    private Long vehicleId;

    @TableField("driver_id")
    private Long driverId;

    @TableField("applicant_id")
    private Long applicantId;

    @TableField("applicant_name")
    private String applicantName;

    @TableField("apply_dept")
    private String applyDept;

    @TableField("start_place")
    private String startPlace;

    @TableField("end_place")
    private String endPlace;

    @TableField("use_date")
    private LocalDate useDate;

    @TableField("start_time")
    private LocalTime startTime;

    @TableField("end_time")
    private LocalTime endTime;

    @TableField("purpose")
    private String purpose;

    @TableField("passenger_count")
    private Integer passengerCount;

    @TableField("status")
    private Integer status;

    @TableField("approve_id")
    private Long approveId;

    @TableField("approve_name")
    private String approveName;

    @TableField("approve_time")
    private LocalDateTime approveTime;

    @TableField("approve_remark")
    private String approveRemark;

    @TableField("actual_mileage")
    private BigDecimal actualMileage;

    @TableField("remark")
    private String remark;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String plateNumber;

    @TableField(exist = false)
    private String driverName;
}