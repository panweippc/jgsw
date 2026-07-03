package com.jgsw.office.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("warehouse")
public class Warehouse {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("warehouse_name")
    private String warehouseName;

    @TableField("warehouse_code")
    private String warehouseCode;

    @TableField("location")
    private String location;

    @TableField("manager")
    private String manager;

    @TableField("phone")
    private String phone;

    @TableField("capacity")
    private BigDecimal capacity;

    @TableField("description")
    private String description;

    @TableField("sort_order")
    private Integer sortOrder;

    @TableField("status")
    private Integer status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
