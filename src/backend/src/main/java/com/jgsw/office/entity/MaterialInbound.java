package com.jgsw.office.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("material_inbound")
public class MaterialInbound {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("inbound_no")
    private String inboundNo;

    @TableField("material_id")
    private Long materialId;

    @TableField("warehouse_id")
    private Long warehouseId;

    @TableField("quantity")
    private Integer quantity;

    @TableField("unit_price")
    private BigDecimal unitPrice;

    @TableField("total_amount")
    private BigDecimal totalAmount;

    @TableField("supplier")
    private String supplier;

    @TableField("purchase_no")
    private String purchaseNo;

    @TableField("batch_no")
    private String batchNo;

    @TableField("expire_date")
    private LocalDateTime expireDate;

    @TableField("operator_id")
    private Long operatorId;

    @TableField("operator_name")
    private String operatorName;

    @TableField("remark")
    private String remark;

    @TableField("status")
    private Integer status;

    @TableField("create_time")
    private LocalDateTime createTime;
}
