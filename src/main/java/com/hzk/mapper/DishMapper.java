package com.hzk.mapper;

import com.hzk.entity.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86136
* @description 针对表【dish】的数据库操作Mapper
* @createDate 2023-12-12 21:34:04
* @Entity com.hzk.entity.Dish
*/
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}




