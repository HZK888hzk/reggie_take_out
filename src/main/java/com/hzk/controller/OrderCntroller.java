package com.hzk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzk.entity.Orders;
import com.hzk.service.OrdersService;
import com.hzk.util.BaseContext;
import com.hzk.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单表的controller
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderCntroller {


    @Autowired
    private OrdersService ordersService;

    /**
     * 订单支付的功能
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public Result<String> submit1(@RequestBody Orders orders){
        log.info("接收到的数据是：{}",orders);
        log.info("订单数据：{}",orders);
        ordersService.order(orders);
        return Result.success("下单成功");
    }
    /**
     * 查看订单的功能
     */
    @GetMapping("/userPage")
    public Result<Page> selectByPage(int page , int pageSize){
        log.info("接收到的数据为：{}",page);
        Long id = BaseContext.getCurrentId();
        Page<Orders> ordersPage = new Page<>(page ,pageSize);
        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Orders::getUserId,id);
        Page<Orders> ordersPage1 = ordersService.page(ordersPage, lambdaQueryWrapper);
        return Result.success(ordersPage1);
    }
}
