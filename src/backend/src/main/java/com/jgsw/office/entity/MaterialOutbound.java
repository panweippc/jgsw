package com.jgsw.office.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("material_outbound")
public class MaterialOutbound {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("outbound_no")
    private String outboundNo;

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

    @TableField("receiver_dept")
    private String receiverDept;

    @TableField("receiver_id")
    private Long receiverId;

    @TableField("receiver_name")
    private String receiverName;

    @TableField("apply_no")
    private String applyNo;

    @TableField("purpose")
    private String purpose;

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
