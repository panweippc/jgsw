package com.jgsw.office.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("material_stock")
public class MaterialStock {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("material_id")
    private Long materialId;

    @TableField("warehouse_id")
    private Long warehouseId;

    @TableField("quantity")
    private Integer quantity;

    @TableField("available_quantity")
    private Integer availableQuantity;

    @TableField("locked_quantity")
    private Integer lockedQuantity;

    @TableField("last_inbound_time")
    private LocalDateTime lastInboundTime;

    @TableField("last_outbound_time")
    private LocalDateTime lastOutboundTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
