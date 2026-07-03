package com.jgsw.office.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("driver")
public class Driver {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("driver_name")
    private String driverName;

    @TableField("phone")
    private String phone;

    @TableField("driver_license")
    private String driverLicense;

    @TableField("license_type")
    private String licenseType;

    @TableField("license_expire")
    private LocalDate licenseExpire;

    @TableField("hire_date")
    private LocalDate hireDate;

    @TableField("department")
    private String department;

    @TableField("status")
    private Integer status;

    @TableField("remark")
    private String remark;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}