package com.hzk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzk.entity.OrderDetail;
import com.hzk.service.OrderDetailService;
import com.hzk.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author 86136
* @description 针对表【order_detail】的数据库操作Service实现
* @createDate 2024-01-25 16:56:11
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}




