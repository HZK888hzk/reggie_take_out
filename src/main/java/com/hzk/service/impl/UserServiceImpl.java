package com.hzk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzk.entity.User;
import com.hzk.service.UserService;
import com.hzk.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 86136
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-01-15 14:31:58
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




