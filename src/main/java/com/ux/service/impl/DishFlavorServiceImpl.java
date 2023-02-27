package com.ux.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ux.entiry.Dish;
import com.ux.entiry.DishFlavor;
import com.ux.mapper.DishFlavorMapper;
import com.ux.mapper.DishMapper;
import com.ux.service.DishFlavorService;
import com.ux.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {

}
