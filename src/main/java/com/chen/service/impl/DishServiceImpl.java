package com.chen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.dto.DishDto;
import com.chen.mapper.DishMapper;
import com.chen.pojo.Dish;
import com.chen.pojo.DishFlavor;
import com.chen.service.DishFlavorService;
import com.chen.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    // 将 DishDto 的数据保存到菜品表和菜品口味表中



    @Autowired
    private DishFlavorService dishFlavorService;

    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {

        // 保存菜品的基本信息到菜品表
        super.save(dishDto);

        Long dishId = dishDto.getId();


        List<DishFlavor> flavors = dishDto.getFlavors();


        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);


    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = super.getById(id);

        DishDto dishDto = new DishDto();

        BeanUtils.copyProperties(dish, dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());

        List<DishFlavor> list = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(list);
        return dishDto;
    }

    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        super.updateById(dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        //获取前端提交的flavor数据
        List<DishFlavor> flavors = dishDto.getFlavors();

        //将每条flavor的dishId赋值
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());


        //将数据批量保存到dish_flavor数据库
        dishFlavorService.saveBatch(flavors);


    }
}
