package com.hzk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzk.entity.Employee;
import com.hzk.service.EmployeeService;
import com.hzk.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

/**
* @author 86136
* @description 针对表【employee】的数据库操作Service实现
* @createDate 2023-12-07 21:54:25
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
        implements EmployeeService{

}




