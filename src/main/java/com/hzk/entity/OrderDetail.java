package com.hzk.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @TableName order_detail
 */
@TableName(value ="order_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail implements Serializable {
    private Long id;

    private String name;

    private String image;

    private Long orderId;

    private Long dishId;

    private Long setmealId;

    private String dishFlavor;

    private Integer number;

    private BigDecimal amount;

    private static final long serialVersionUID = 1L;


}