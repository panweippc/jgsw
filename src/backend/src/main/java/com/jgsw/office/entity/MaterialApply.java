package com.jgsw.office.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("material_apply")
public class MaterialApply {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("apply_no")
    private String applyNo;

    @TableField("material_id")
    private Long materialId;

    @TableField("quantity")
    private Integer quantity;

    @TableField("purpose")
    private String purpose;

    @TableField("apply_dept")
    private String applyDept;

    @TableField("apply_id")
    private Long applyId;

    @TableField("apply_name")
    private String applyName;

    @TableField("apply_time")
    private LocalDateTime applyTime;

    @TableField("warehouse_id")
    private Long warehouseId;

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

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
