package com.chen.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.common.CustomException;
import com.chen.common.R;
import com.chen.mapper.CategoryMapper;
import com.chen.mapper.DishMapper;
import com.chen.mapper.SetmealMapper;
import com.chen.pojo.Category;
import com.chen.pojo.Dish;
import com.chen.pojo.Setmeal;
import com.chen.service.CategoryService;
import com.chen.service.DishService;
import com.chen.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public R<String> saveSortInfo(Category category) {
        categoryMapper.insert(category);
        return R.success("新增分类成功！");
    }

    @Override
    public R<String> deleteSortInfo(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count1 = dishMapper.selectCount(dishLambdaQueryWrapper);

        if (count1 > 0) {
            throw new CustomException("当前分类下关联了菜品，不能删除！");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealMapper.selectCount(setmealLambdaQueryWrapper);

        if (count2 > 0) {
            throw new CustomException("当前分类下套餐了菜品，不能删除！");
        }

        super.removeById(id);
        return R.success("分类信息删除成功！");
    }

    @Override
    public R<String> updateSortInfo(Category category) {
        categoryMapper.updateById(category);
        return R.success("修改完成！");
    }

    @Override
    public R<Page> pageSortInfo(int page, int pageSize) {
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        categoryMapper.selectPage(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }


    @Override
    public R<List<Category>> getListInfo(Category category) {

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());

        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> categories = categoryMapper.selectList(queryWrapper);

        return R.success(categories);
    }

}
