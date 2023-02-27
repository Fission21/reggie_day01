package com.ux.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ux.entiry.Dish;
import com.ux.entiry.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

/**
 菜品
 */
@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {

}
