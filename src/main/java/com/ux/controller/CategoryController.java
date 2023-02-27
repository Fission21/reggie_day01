package com.ux.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ux.common.R;
import com.ux.entiry.Category;
import com.ux.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 以json格式传送回来，需要用RequestBody来获取
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category :{}",category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> pageR(int page,int pageSize){
        // 分页构造器
        Page<Category> pageInfo = new Page<>(page,pageSize);
        // 条件构造器，这是使用的原因是因为Category中有一个sort排序
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 添加排序条件，根据sort进行排序
        queryWrapper.orderByAsc(Category::getSort); //orderByAsc 升序排序
        // 进行分页查询
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据Id删除分类
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("id为，{}",ids);
        categoryService.remove(ids);
        return R.success("删除成功");
    }

    /**
     * 修改分类
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改信息分类成功");
    }

    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        // 条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper();
        // 添加条件
        lambdaQueryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        // 添加排序条件
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(lambdaQueryWrapper);
        return R.success(list);
    }

}
