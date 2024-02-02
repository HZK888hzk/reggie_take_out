package com.hzk.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 
 * @TableName employee  员工实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="employee")
public class Employee implements Serializable {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码 （MD5加密了）
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别
     */
    private String sex;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 状态 0:禁用，1:正常
     */
    private Integer status;

    @TableField(fill = FieldFill.INSERT) //这个file后面填充的策略 ，插入时填充
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)  //插入和更新时填充
    private Date updateTime;
    @TableField(fill = FieldFill.INSERT)     //插入时填充
    private Long createUser;
    @TableField(fill = FieldFill.INSERT_UPDATE)  //插入和更新时填充
    private Long updateUser;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}