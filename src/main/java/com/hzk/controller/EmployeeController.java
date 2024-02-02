package com.hzk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzk.entity.Employee;
import com.hzk.mapper.EmployeeMapper;
import com.hzk.service.EmployeeService;
import com.hzk.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private  EmployeeService employeeService;

    /**
     * 登录的方法
     * @param request  这个是为了登录成功以后把对象放在sesseion中，便于以后的获取
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

            //前端已经提交过来密码了。我们先使用MD5进行加密处理
            String password = employee.getPassword();
            //将密码使用MD5加密
             password = DigestUtils.md5DigestAsHex(password.getBytes());
            //加密完后与数据库中的密码进行对比
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
            //这个就是一个条件，如同where后面的条件
            queryWrapper.eq(Employee::getUsername,employee.getUsername());
             Employee employee1 = employeeService.getOne(queryWrapper);
             if (employee1==null){
                 return Result.error("用户名不存在");
             }else {
                 //账号存在，判断密码是否正确
                if (!employee1.getPassword().equals(password)){
                    return Result.error("密码不正确");
                }else {
                    //密码正确的话，判断这个账号的状态是启用还是禁用状态
                    if (employee1.getStatus()==0){
                        return Result.error("用户处于禁用状态，不可登录");
                    }else {
                        //用户名存在，密码正确，状态等于1
                       request.getSession().setAttribute("employee",employee1.getId());
                    }
                }
             }
                return Result.success(employee1);
        }

    /**
     * 退出登录/logout
     */
    @PostMapping ("/logout")
    public Result logout(HttpServletRequest request){
        //清除session中的  用户id，完后跳转到index。html
        request.getSession().removeAttribute("employee");
        //就是把code改为1，完后前端判断了如果等于1的话，就会跳转到登录页面
        return Result.success("退出成功") ;
    }

    /**
     * 员工管理的分页查询
     */
    @GetMapping("/page")
    public  Result<Page> selectByPage(int page, int pageSize ,String name){
        log.info("接受到的数据是 {}" ,page ,pageSize);
        //构造分页构造器
        Page pageInfo = new Page(page ,pageSize);
        //根据name查询的时候，需要构造一个条件构造器
        LambdaQueryWrapper<Employee> queryWrapper =new LambdaQueryWrapper();
        //添加过滤条件
        //第二个字段是，我们实体类的字段
        queryWrapper.like(!StringUtils.isEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo,queryWrapper);
        return Result.success(pageInfo);
    }

    /**
    * 新增员工的接口
    */
    @PostMapping
    public Result<String> insertEmployee(HttpServletRequest request,@RequestBody Employee employee){
        //设置初始密码为123456，需要进行加密
        String password = DigestUtils.md5DigestAsHex("123456".getBytes());

        //先看一下能否封装到这个实体类中
        log.info("新增员工，员工信息:{} ",employee.toString());
        try {
            employeeService.save(employee);
        }catch (Exception e){
            return Result.error("新增员工失败,账号已存在，请更换");
        }

        return Result.success("新增员工成功");
    }

    /**
     * 禁用员工（根据用户id去修改用户的状态）
     * 根据id修改员工信息
     *
     */
    @PutMapping
    public Result<String> updateUesr( HttpServletRequest request ,@RequestBody Employee employee){
            employeeService.updateById(employee);
            return Result.success("用户状态修改成功");

    }

    /**
     * 根据id修改员工信息
     * @return
     */
    @GetMapping("/{id}")
    public Result<Employee> updateById(@PathVariable Long id){
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Employee::getId,id);
       //先根据用户id查找这个用户的信息
        Employee employee = employeeService.getById(id);
        log.info("查找的信息有:{}",employee);

        if (employee !=null){
            return Result.success(employee);
        }
        else return Result.error("没有当前员工信息");
    }



}

