package com.hzk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzk.dto.DishDto;
import com.hzk.entity.Dish;
import com.hzk.entity.DishFlavor;
import com.hzk.service.DishFlavorService;
import com.hzk.service.DishService;
import com.hzk.mapper.DishMapper;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

/**
* @author 86136
* @description 针对表【dish】的数据库操作Service实现
* @createDate 2023-12-12 21:34:05
*/
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
    implements DishService{
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品
     * @param dishDto 封装的实体类
     * @return
     */
    @Override
    public void saveByHHH(DishDto dishDto) {
        //把数据先保存到dish表中
        dishService.save(dishDto);

        //得到菜品的id
        Long id = Long.valueOf(dishDto.getId());
        //保存菜品口味到味道表
        List<DishFlavor> flavors = dishDto.getFlavors();
        //遍历出来我们得到的口味，使用迭代器遍历,遍历出来完后给每一个数据设置一id
        Iterator<DishFlavor> iterator = flavors.iterator();
        while (iterator.hasNext()){
            DishFlavor next = iterator.next();
            next.setDishId(id);
        }
        dishFlavorService.saveBatch(flavors);
    }
    /*
    * 修改菜品之前的查询回来的信息
    * */
    @Override
    public DishDto selectByid(Long id) {
        //先获取dish
        Dish dish = dishService.getById(id);
        //一般情况下，当一个实体类中，没有另一个实体类中的属性的时候，一般使用拷贝
        //对象拷贝,就是dish中的数据拷贝给DishDto
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //获取在dishFlaver表中的dishid
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,dish.getId());
//      口味也已经查询出来了
        List<DishFlavor> flavors = dishFlavorService.list(lambdaQueryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }
    /*
    * 修改菜品
    * */
    @Transactional
    @Override
    public void updateDish(DishDto dishDto) {
        //先修改dish这个里面的数据
        dishService.updateById(dishDto);
        Long id = dishDto.getId();
        /*
        * //现在修改dishFlaver中的数据
        * 一种思路就是：
        * 1、先把当前id的数据的口味全部清除
        * 2、完后再插入现在封装过来的口味
        * */
//        1、先把当前id的数据的口味全部清除
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        //完后再插入现在封装过来的口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        Iterator<DishFlavor> iterator = flavors.iterator();
        while (iterator.hasNext()){
            DishFlavor next = iterator.next();
            next.setDishId(id);
        }
        dishFlavorService.saveBatch(flavors);

    }

}




