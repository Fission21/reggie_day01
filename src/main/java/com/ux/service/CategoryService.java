package com.ux.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ux.entiry.Category;

public interface CategoryService extends IService<Category> {
    /**
     * 删除菜品和套餐前，必须判断是否有关联菜品
     * @param id
     */
    void remove(Long id);
}
