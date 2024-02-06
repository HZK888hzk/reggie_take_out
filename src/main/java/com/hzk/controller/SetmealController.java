package com.hzk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzk.dto.SeteamlDto;
import com.hzk.entity.Setmeal;
import com.hzk.service.SetmealService;
import com.hzk.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 套餐管理的控制层
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 分页查询套餐信息
     * 这里查询时两个表的数据
     * 一个是setmeal 、category表的数据
     */
    @GetMapping("/page")
    public Result<Page> selectList(int page , int pageSize , String name){
        log.info("接受的page {}",page);
        log.info("接受的pageSize {}",pageSize);
        log.info("接受的name {}",name);
        Page<Setmeal> page1 = new Page<>();
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(name !=null,Setmeal::getName,name);
        setmealService.page(page1,lambdaQueryWrapper);
        return Result.success(page1);
    }
    /**
     * 新增套餐
     */
    @PostMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    public Result<String> InsertTTT(@RequestBody SeteamlDto seteamlDto){
        log.info("接收到的数据：{}",seteamlDto);
        setmealService.insertByHzk(seteamlDto);
        return Result.success("新增套餐成功");
    }
    /**8
     * 新增之前把东西查询出来
     */
    @GetMapping("/{id}")
    public Result<SeteamlDto> findById(@PathVariable Long id){
        log.info("得到的id是 {}",id);
        //因为涉及到两个表的的表的数据，所以需要在service实现类中重新编写方法
        SeteamlDto seteamlDto = setmealService.findByDto(id);
        return Result.success(seteamlDto);
    }
    /**
     * 修改数据
     */
    @PutMapping
    public Result<String> updateById(@RequestBody SeteamlDto seteamlDto){
        log.info("接收到的对象是 {}", seteamlDto);
        //因为涉及到两个表的的表的数据，所以需要在service实现类中重新编写方法
        setmealService.insertById(seteamlDto);
        return Result.success("套餐修改成功");
    }
    /**
     * 删除或者批量删除
     * 售卖的状态下不能删除，必须先停售才能删除
     * 删除套餐的时候还需要删除套餐的关联表的数据
     *
     * @CacheEvict(value = "setmealCache",allEntries = true)
     * 就是当实现删除操作就清除这个缓存，allEntries = true指的是清除所有的缓存
     */
    @DeleteMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    public Result<String> delectByIds(@RequestParam List<Long> ids){
        log.info("接收的id{}",ids);
        setmealService.removeWithDish(ids);
        return Result.success("商品删除成功");
    }
    /**
     * 改变套装的状态
     */
    @PostMapping("/status/{status}")
    public Result<String> updateByStatus(@PathVariable int status ,@RequestParam List<Long> ids){
        log.info("接收到的status {}",status);
        log.info("接收到的id是 {}",ids);
        List<Setmeal> setmeals = setmealService.listByIds(ids);
        for (Setmeal setmeal:setmeals){
            setmeal.setStatus(status);
            setmealService.updateById(setmeal);
        }
        return Result.success("状态修改成功");
    }
    /**
     * 移动端的套餐查询
     * value 就是缓存的名称
     * key 就是缓存id  ，就是一个缓存名称下有多个缓存，根据id来区分
     * 这个id一般就是多个查询条件的组合
     * @Cacheable(value = "" ,key = ”“)
     * #setmeal就是获取当前对象
     * 这个Result返回结果要实现序列化
     */
    @GetMapping("/list")
    @Cacheable(value = "setmealCache",key = "#setmeal.categoryId +'_' +#setmeal.status")
    public Result<List<Setmeal>> list(Setmeal setmeal){
        log.info("接收到的套餐id室{}",setmeal.getCategoryId());
        Long categoryId = setmeal.getCategoryId();
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(setmeal.getCategoryId() !=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        lambdaQueryWrapper.eq(Setmeal::getStatus,1);
        List<Setmeal> setmealList = setmealService.list(lambdaQueryWrapper);
        return Result.success(setmealList);
    }
}
