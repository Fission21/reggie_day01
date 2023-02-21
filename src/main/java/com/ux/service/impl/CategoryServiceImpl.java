package com.ux.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ux.entiry.Category;
import com.ux.entiry.Employee;
import com.ux.mapper.CategoryMapper;
import com.ux.mapper.EmployeeMapper;
import com.ux.service.CategoryService;
import com.ux.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

}
