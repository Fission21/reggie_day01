package com.ux.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ux.entiry.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
