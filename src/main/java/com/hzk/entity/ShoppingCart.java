package com.hzk.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @TableName shopping_cart
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="shopping_cart")
public class ShoppingCart implements Serializable {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 图片
     */
    private String image;

    /**
     * 主键
     */
    private Long userId;

    /**
     * 菜品id
     */
    private Long dishId;

    /**
     * 套餐id
     */
    private Long setmealId;

    /**
     * 口味
     */
    private String dishFlavor;

    /**
     * 数量
     */
    private Integer number;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 创建时间
     */

    private Date createTime;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}