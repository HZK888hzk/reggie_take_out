package com.hzk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzk.dto.DishDto;
import com.hzk.entity.Category;
import com.hzk.entity.Dish;
import com.hzk.entity.DishFlavor;
import com.hzk.service.CategoryService;
import com.hzk.service.DishFlavorService;
import com.hzk.service.DishService;
import com.hzk.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询全部,查询多张表的数据完后封装在一个page中
     * @param page
     * @param pageSize
     * @param name 就是根据条件查询时的那个条件
     * @return
     */
    @GetMapping("/page")
    public Result<Page<Dish>> selectAll(Long page , Long pageSize, String name){
        //创建一个这个类
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        //当前类中有Dish类中没有的属性
        Page<DishDto> objectPage = new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //根据条件查找
        queryWrapper.like(name !=null,Dish::getName,name);

        dishService.page(pageInfo,queryWrapper);

        return Result.success(pageInfo);

    }
    /**
     * 批量或者单独删除菜品信息
     */
    @DeleteMapping
    public Result<String> delect(@RequestParam List<String> ids){
        log.info("得到的id是{}",ids);
        dishService.removeByIds(ids);
        return Result.success("删除信息成功");
    }

    /**
     * 菜品管理那那一页，修改之前先把数据查询出来
     */
    @GetMapping("/{id}")
    public Result<DishDto> selectList(@PathVariable Long id){
        log.info("得到的id{}",id);
        DishDto dishDto = dishService.selectByid(id);
        return Result.success(dishDto);

    }
    /**
     * 上面查询出来，完后修改菜品
     */
    @PutMapping
    public Result<String> insertBySave(@RequestBody DishDto dishDto){
        dishService.updateDish(dishDto);
        return Result.success("菜品修改成功");
    }

    /**
     * 新增菜品,
     * 把菜品信息插入到dish表中
     * 把口味信息插入到DishFlavor中
     */
    @PostMapping
    public Result<String> insert(@RequestBody DishDto dishDto){
        log.info("写入的信息为{}",dishDto);
        dishService.saveByHHH(dishDto);
         return Result.success("新增菜品成功");
    }

    /**
     * 更改菜品状态
     */
    @PostMapping("/status/{status}")
    public Result<String> update(@PathVariable("status") Integer status ,@RequestParam List<Long> ids){
        log.info("获得到的id是{}" ,ids);
        log.info("获得到的status是{}" ,status);
        for (Dish dish : dishService.listByIds(ids)) {
            //这里得到了dish,但是还没有修改dish中的status的值
            dish.setStatus(status);
            dishService.updateById(dish);
        }
        return Result.success("菜品状态更改成功");
    }
    /**
     * 在添加套餐哪里有一个菜品展示
     * 就是根据categoryId来查询菜品信息
     */
   /* @GetMapping("/list")
    public Result<List<Dish>> selectById(Dish dish){
        log.info("接收到的id是 {}",dish.getId());
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(dish.getCategoryId() !=null,Dish::getCategoryId,dish.getId());
        lambdaQueryWrapper.eq(Dish::getStatus,1);
        List<Dish> dishList = dishService.list(lambdaQueryWrapper);
        return Result.success(dishList);
    }*/

    @GetMapping("/list")
    public Result<List<DishDto>> selectById(Dish dish){
        log.info("接收到的id是 {}",dish.getId());
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(dish.getCategoryId() !=null,Dish::getCategoryId,dish.getCategoryId());
        lambdaQueryWrapper.eq(Dish::getStatus,1);
        List<Dish> dishList = dishService.list(lambdaQueryWrapper);
        //遍历一下这个list
        List<DishDto> dishDtoList = dishList.stream().map((item)-> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            //现在查询口味
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> flavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            flavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);

            List<DishFlavor> dishFlavors = dishFlavorService.list(flavorLambdaQueryWrapper);
            dishDto.setFlavors(dishFlavors);

            return dishDto;
        }).collect(Collectors.toList());

        return Result.success(dishDtoList);
    }

}
