package com.hzk.service;

import com.hzk.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hzk.util.Result;

/**
* @author 86136
* @description 针对表【orders】的数据库操作Service
* @createDate 2024-01-25 16:55:08
*/
public interface OrdersService extends IService<Orders> {

    /**
     * 订单支付功能
     */
    public Result<String> order(Orders orders);
}
