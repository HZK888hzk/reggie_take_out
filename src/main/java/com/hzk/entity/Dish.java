package com.hzk.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 
 * @TableName dish
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="dish")
public class Dish implements Serializable {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 菜品名称
     */
    private String name;

    /**
     * 菜品分类id
     */
    private Long categoryId;

    /**
     * 菜品价格
     */
    private BigDecimal price;

    private String code;

    private String image;

    private String description;

    private Integer status;

    private Integer sort;

    //使用到公共字段的填充
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    @TableField(fill =FieldFill.INSERT_UPDATE)
    private Long updateUser;
    //逻辑删除的字段
    //没有删除是0 ，删除是1
    @TableLogic(value = "0",delval = "1")
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}