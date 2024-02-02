package com.hzk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzk.entity.Category;
import com.hzk.entity.Dish;
import com.hzk.entity.Setmeal;
import com.hzk.service.CategoryService;
import com.hzk.service.DishService;
import com.hzk.service.SetmealService;
import com.hzk.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    /**
     * 分页查询全部
     */
    @GetMapping("/page")
    public Result<Page> ListAll(int page, int pageSize){
        Page pageInfo = new Page(page,pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo,queryWrapper);
        return Result.success(pageInfo);

    }
    @PostMapping
    public Result<String> insertCategory(HttpServletRequest request, @RequestBody Category category){
        //这里使用了公共字段填充，不需要重新设置值
        log.info("新增的套餐为：{}" ,category.toString() );
        //先判断以下添加的这个菜品是否存在
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Category::getName,category.getName());
        Category category1 = categoryService.getOne(queryWrapper);
        if (category1 ==null){
            categoryService.save(category);
            return Result.success("新增成功");
        }else
            return Result.error("当前菜品/套餐已存在");

    }
    /**
     * 修改分类
     */
    @PutMapping
    public Result<String> update(HttpServletRequest request, @RequestBody Category category){
        //这里使用了公共字段填充，不需要重新设置值
        //设置数据库中不为空的字段，手动设置。
        //查询下修改的类名是否存在
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Category::getName,category.getName());
        Category category1 = categoryService.getOne(queryWrapper);
        if (category1== null){
            categoryService.updateById(category);
            return Result.success("分类修改成功");
        }
        return Result.error("分类已经存在，请重新修改");
    }
    /**
     * 根据id删除,删除之前判断这个套餐下面是否有菜
     */

    @DeleteMapping
    public Result<String> deleteByIds(Long ids){
        log.info("获取的id是：{}",ids);
        //根据这个主键去查询套餐下面是否有东西
        LambdaQueryWrapper<Dish> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,ids);
        List<Dish> dishList = dishService.list(queryWrapper);

        LambdaQueryWrapper<Setmeal> queryWrapper1 =new LambdaQueryWrapper<>();
        queryWrapper1.eq(Setmeal::getCategoryId ,ids);
        List<Setmeal> setmealList = setmealService.list(queryWrapper1);
        if (dishList.isEmpty()&&setmealList.isEmpty()){
            //如果没有东西，可以删除分类
            categoryService.removeById(ids);
            return Result.success("删除分类成功");
        }
        return Result.error("当前分类下有菜品或者套餐，不能删除");

    }

    /**
     * 菜品管理那那一页，修改之前先把数据查询出来
     */

    @GetMapping("/list")
    public Result<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        return Result.success(list);
    }

}
