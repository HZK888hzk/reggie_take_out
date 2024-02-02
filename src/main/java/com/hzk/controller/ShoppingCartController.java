package com.hzk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hzk.entity.ShoppingCart;
import com.hzk.service.ShoppingCartService;
import com.hzk.util.BaseContext;
import com.hzk.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * 购物车的cortroller
 */
@Slf4j
@RestController
@RequestMapping("shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 移动端的往购物车中添加数据
     * @param shoppingCart
     * @param session
     * @return
     */
    @PostMapping("/add")
    public Result<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart , HttpSession session){
        log.info("接收到的购物车数据{}",shoppingCart);
        //1、先获取到用户的id
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        //2、判断当前菜品是否在购物车中，根据菜品id，或者套餐id来实现查询
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        if (dishId !=null){
            //说明添加到购物车的是菜品，则判断一下购物车中是否有当前菜品，如果有的话，则++
            lambdaQueryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            //在这说明的是添加的是套餐
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
            }

        ShoppingCart shoppingCartList = shoppingCartService.getOne(lambdaQueryWrapper);
        if (shoppingCartList !=null){
            Integer number = shoppingCartList.getNumber();
            shoppingCartList.setNumber(number+1);
            shoppingCartService.updateById(shoppingCartList);
        }else{
            //在这里说明购物车中没有当前菜品
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(new Date());
            shoppingCartService.save(shoppingCart);
            shoppingCartList =shoppingCart;
        }
            return Result.success(shoppingCartList);
        }

    /**
     * 就是根据用户id来查询当前用户的饿购物车
     * @return
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        //排序以后就可以做到最先加进来的菜品最先展示
        lambdaQueryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(lambdaQueryWrapper);
        return Result.success(shoppingCartList);
    }
    /**
     * 购物车中减去东西的方法
     * 根据用户id和菜品或者套餐id去联合相减
     */
    @PostMapping("/sub")
    public Result<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        log.info("接收的数据为：{}",shoppingCart);
        //先获取到用户id
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        if (shoppingCart.getDishId() != null){
            lambdaQueryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else {
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        //先查询一下是否存在
        ShoppingCart shoppingCart1 = shoppingCartService.getOne(lambdaQueryWrapper);
        if (shoppingCart1 !=null){
            Integer number = shoppingCart1.getNumber();
            shoppingCart1.setNumber(number-1);
            shoppingCartService.updateById(shoppingCart1);
            if (number==1){
                shoppingCartService.remove(lambdaQueryWrapper);
            }
        }
        shoppingCart = shoppingCart1;
        return Result.success(shoppingCart);
    }

    /**
     * 根据用户id去清除全部的购物车
     * @return
     */
    @DeleteMapping("/clean")
    public Result<String> clean(ShoppingCart shoppingCart){
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        shoppingCartService.remove(lambdaQueryWrapper);
        return Result.success("购物车全部清除完成");
    }

}

