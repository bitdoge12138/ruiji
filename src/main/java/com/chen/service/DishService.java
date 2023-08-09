package com.chen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.dto.DishDto;
import com.chen.pojo.Dish;

public interface DishService extends IService<Dish> {

    void saveWithFlavor(DishDto dishDto);

    DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);


}
