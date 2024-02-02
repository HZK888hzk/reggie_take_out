package com.hzk.mapper;

import com.hzk.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86136
* @description 针对表【category】的数据库操作Mapper
* @createDate 2023-12-11 16:44:28
* @Entity com.hzk.entity.Category
*/
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}




