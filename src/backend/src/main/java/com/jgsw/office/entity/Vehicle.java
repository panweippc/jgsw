package com.jgsw.office.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("vehicle")
public class Vehicle {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("plate_number")
    private String plateNumber;

    @TableField("vehicle_type")
    private String vehicleType;

    @TableField("brand")
    private String brand;

    @TableField("model")
    private String model;

    @TableField("color")
    private String color;

    @TableField("seat_count")
    private Integer seatCount;

    @TableField("purchase_date")
    private LocalDate purchaseDate;

    @TableField("mileage")
    private BigDecimal mileage;

    @TableField("fuel_type")
    private String fuelType;

    @TableField("engine_no")
    private String engineNo;

    @TableField("vin")
    private String vin;

    @TableField("insurance_expire")
    private LocalDate insuranceExpire;

    @TableField("inspection_expire")
    private LocalDate inspectionExpire;

    @TableField("status")
    private Integer status;

    @TableField("remark")
    private String remark;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}