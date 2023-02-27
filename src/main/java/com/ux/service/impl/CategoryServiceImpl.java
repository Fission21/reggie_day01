package com.ux.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ux.common.CustomException;
import com.ux.entiry.Category;
import com.ux.entiry.Dish;
import com.ux.entiry.Setmeal;
import com.ux.mapper.CategoryMapper;
import com.ux.service.CategoryService;
import com.ux.service.DishService;
import com.ux.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;
    /**
     * 根据id删除分类，删除之前需要进行判断，判断分类是否关联菜品
     * @param id
     */
    @Override
    public void remove(Long id) {
        // 查询当前分类是否关联菜品，如果已经关联，抛出一个异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件，根据分类id进行查询
        int count = dishService.count(dishLambdaQueryWrapper.eq(Dish::getCategoryId, id));
        if (count > 0){
            // 已经关联菜品，抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }

        // 查询当前分类是否关联了套餐，如果已经关联，抛出一个异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        int count1 = setmealService.count(setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id));
        if (count1 > 0){
            // 已经关联一个套餐，抛出一个异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        // 都没有关联，正常删除
        super.removeById(id);
    }
}
