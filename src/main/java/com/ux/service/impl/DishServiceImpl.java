package com.ux.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ux.dto.DishDto;
import com.ux.entiry.Dish;
import com.ux.entiry.DishFlavor;
import com.ux.mapper.DishMapper;
import com.ux.service.DishFlavorService;
import com.ux.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    DishFlavorService dishFlavorService;
    /**
     * 新增菜品，同时保存对应的口味数据
     * @param dishDto
     */
    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        // 保存菜品的基本信息到菜品表dish
        this.save(dishDto);

        Long id = dishDto.getId(); // 菜品id

        // 菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) ->{
           item.setDishId(id);
           return item;
        }).collect(Collectors.toList());

        // 保存菜品口味数据到菜品口味dish_flavor, 批量保存 saveBatch
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id来查询菜品信息和口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        // 1.查询菜品基本信息 dish
        Dish dish = this.getById(id);

        // 拷贝对象
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        // 2.查询当前菜品对应的口味信息 dish_flavor
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavor = dishFlavorService.list(dishFlavorLambdaQueryWrapper);

        dishDto.setFlavors(flavor);

        return dishDto;
    }

    /**
     * 更新
     * @param dishDto
     */
    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        // 更新dish基本信息
        this.updateById(dishDto);

        // 清理当前菜品对应口味数据-- dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        // 添加当前提交过来的口味数据-- dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) ->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }
}
