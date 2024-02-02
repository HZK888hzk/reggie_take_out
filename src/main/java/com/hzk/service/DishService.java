package com.hzk.service;

import com.hzk.dto.DishDto;
import com.hzk.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86136
* @description 针对表【dish】的数据库操作Service
* @createDate 2023-12-12 21:34:05
*/

public interface DishService extends IService<Dish> {

    /**
     * 新增菜品
     */
    public void saveByHHH(DishDto dishDto);

    /**
     * 修改菜品之前的查询回来的信息
     */
    public DishDto selectByid(Long id );

    /**
     * 修改菜品
     */
    public void updateDish(DishDto dishDto);
}
