package com.hzk.dto;

import com.hzk.entity.Setmeal;
import com.hzk.entity.SetmealDish;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 继承于Setmeal，因为那个分页查询有别的表的数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeteamlDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
