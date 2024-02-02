package com.hzk.mapper;

import com.hzk.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86136
* @description 针对表【employee】的数据库操作Mapper
* @createDate 2023-12-07 21:54:25
* @Entity com.hzk.entity.Employee
*/
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}




