package com.ux.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ux.entiry.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 菜品
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}
