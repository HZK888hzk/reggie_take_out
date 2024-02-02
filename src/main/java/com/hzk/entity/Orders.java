package com.hzk.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @TableName orders
 */
@TableName(value ="orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders implements Serializable {
    private Long id;

    private String number;

    private Integer status;

    private Long userId;

    private Long addressBookId;

    private Date orderTime;

    private Date checkoutTime;

    private Integer payMethod;

    private BigDecimal amount;

    private String remark;

    private String phone;

    private String address;

    private String userName;

    private String consignee;

    private static final long serialVersionUID = 1L;


}