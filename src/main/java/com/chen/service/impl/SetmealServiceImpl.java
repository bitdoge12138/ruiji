package com.chen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.common.CustomException;
import com.chen.dto.SetmealDto;
import com.chen.mapper.SetmealMapper;
import com.chen.pojo.Setmeal;
import com.chen.pojo.SetmealDish;
import com.chen.service.SetmealDishService;
import com.chen.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {


    @Autowired
    private SetmealDishService setmealDishService;
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {

        save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();


        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());


        setmealDishService.saveBatch(setmealDishes);


    }

    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //select count(*) from setmeal where ids in(1,2,3) and status = 1
        //查询套餐状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count = super.count(queryWrapper);

        if (count > 0){
            //如果不能删除，抛出一个业务异常
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        //如果可以删除，先删除套餐表中的数据
        super.removeByIds(ids);

        //删除关系表中的数据
        //delete from setmeal_dish where setmeal_id in(1,2,3)
        LambdaQueryWrapper<SetmealDish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);

        setmealDishService.remove(dishLambdaQueryWrapper);

    }
}
