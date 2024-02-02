package com.hzk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzk.entity.Category;
import com.hzk.service.CategoryService;
import com.hzk.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author 86136
* @description 针对表【category】的数据库操作Service实现
* @createDate 2023-12-11 16:44:28
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




