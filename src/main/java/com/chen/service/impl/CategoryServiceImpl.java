package com.chen.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.common.CustomException;
import com.chen.mapper.CategoryMapper;
import com.chen.pojo.Category;
import com.chen.pojo.Dish;
import com.chen.pojo.Setmeal;
import com.chen.service.CategoryService;
import com.chen.service.DishService;
import com.chen.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    DishService dishService;

    @Autowired
    SetmealService setmealService;



    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();

        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);

        int count1 = dishService.count(dishLambdaQueryWrapper);


        if (count1 > 0) throw new CustomException("当前分类下关联了菜品，不能删除");



        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);

        int count2 = setmealService.count(setmealLambdaQueryWrapper);


        if (count2 > 0) throw new CustomException("当前分类下关联了套餐，不能删除");

        // 这里的super代表的是Iservice接口中实现的removeById方法，因为CategoryService中该方法被重写了
        super.removeById(id);


    }
}
