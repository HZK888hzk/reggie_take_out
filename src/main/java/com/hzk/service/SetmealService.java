package com.hzk.service;

import com.hzk.dto.SeteamlDto;
import com.hzk.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hzk.util.CustomException;

import java.util.List;

/**
* @author 86136
* @description 针对表【setmeal】的数据库操作Service
* @createDate 2023-12-12 21:58:32
*/
public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐
     */
    public void  insertByHzk(SeteamlDto seteamlDto);

    /**
     * 修改之前把数据查询出来
     */
    public SeteamlDto findByDto(Long id);

    /**
     * 修改数据
     * @param seteamlDto
     */
    public void insertById(SeteamlDto seteamlDto);

    /**
     * 删除套餐以及关联表的数据
     */
    public void removeWithDish(List<Long> ids);
}
