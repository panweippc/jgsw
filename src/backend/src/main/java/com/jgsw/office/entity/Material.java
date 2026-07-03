package com.jgsw.office.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("material")
public class Material {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("material_code")
    private String materialCode;

    @TableField("material_name")
    private String materialName;

    @TableField("category_id")
    private Long categoryId;

    @TableField("spec")
    private String spec;

    @TableField("unit")
    private String unit;

    @TableField("brand")
    private String brand;

    @TableField("origin")
    private String origin;

    @TableField("purchase_price")
    private BigDecimal purchasePrice;

    @TableField("retail_price")
    private BigDecimal retailPrice;

    @TableField("min_stock")
    private Integer minStock;

    @TableField("max_stock")
    private Integer maxStock;

    @TableField("description")
    private String description;

    @TableField("status")
    private Integer status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
