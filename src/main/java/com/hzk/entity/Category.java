package com.hzk.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 
 * @TableName category
 */
@TableName(value ="category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category  implements Serializable{

    @TableId
    private Long id;
    private Integer type;

    private String name;
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

   }