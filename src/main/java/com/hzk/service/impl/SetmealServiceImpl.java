package com.hzk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzk.dto.SeteamlDto;
import com.hzk.entity.Setmeal;
import com.hzk.entity.SetmealDish;
import com.hzk.service.SetmealDishService;
import com.hzk.service.SetmealService;
import com.hzk.mapper.SetmealMapper;
import com.hzk.util.CustomException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 86136
* @description 针对表【setmeal】的数据库操作Service实现
* @createDate 2023-12-12 21:58:32
*/
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
    implements  SetmealService{

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;
    /**
     * 新增套餐
     * @param seteamlDto
     * //实现步骤
     * 第一步：先把套餐的基本信息插入到Setmeal表中
     * 第二步：遍历得到菜品，把菜品添加到SetmealDish表中
     */
    @Override
    public void insertByHzk(SeteamlDto seteamlDto) {
        /*第一步：先把套餐的基本信息插入到Setmeal表中*/
        setmealService.save(seteamlDto);
        Long id = seteamlDto.getId();
//       第二步，得到菜品信息
        List<SetmealDish> setmealDishes = seteamlDto.getSetmealDishes();
//        遍历菜品信息，依次插到数据库
        for (SetmealDish setmealDish:setmealDishes){
            setmealDish.setSetmealId(String.valueOf(id));

            setmealDishService.save(setmealDish);
        }

    }
    /*
    * 查询之前把数据查询出来
    * */
    @Override
    public SeteamlDto findByDto(Long id) {
        //第一步：根据id查询出阿里seteanl表中的东西
        Setmeal setmeal = setmealService.getById(id);
        //进行对象的拷贝
        SeteamlDto seteamlDto = new SeteamlDto();
        BeanUtils.copyProperties(setmeal,seteamlDto);
//        Long categoryId = setmeal.getCategoryId();
        //获得id，查询另一个表中的东西
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishList = setmealDishService.list(lambdaQueryWrapper);
        seteamlDto.setSetmealDishes(setmealDishList);

        return seteamlDto;
    }

    /**
     * 修改数据
     * @param seteamlDto
     */
    @Override
    public void insertById(SeteamlDto seteamlDto) {
        //更新setmeal表的基本信息
        this.updateById(seteamlDto);
        //清理当前套餐对应的菜品数据---setmeal_dish表的delete操作
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,seteamlDto.getId());
        setmealDishService.remove(queryWrapper);

        //添加当前提交过来的菜品数据---setmeal_dish表的insert操作
        List<SetmealDish> dishes = seteamlDto.getSetmealDishes();
        dishes = dishes.stream().map((item)->{
            item.setId(IdWorker.getId());
            item.setSetmealId(String.valueOf(seteamlDto.getId()));
            return item;

        }).collect(Collectors.toList());
        setmealDishService.saveBatch(dishes);


    }

    /**
     * 删除套餐以及关联表的数据
     * @param ids
     */
    @Transactional
    @Override
    public void removeWithDish(List<Long> ids)  {
        //第一步：先获取到套餐
        List<Setmeal> setmeals = setmealService.listByIds(ids);
        //遍历一下套餐
        for (Setmeal setmeal :setmeals){
            Integer status = setmeal.getStatus();
            if (status.equals(1)){
                throw new CustomException("商品正在售卖中，请下架");
            }

        }
        //没有售卖，已经下架了,删除当前表中的数据
        setmealService.removeByIds(ids);

        //同时删除关联表中的数据
    LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lambdaQueryWrapper);
    }

}




