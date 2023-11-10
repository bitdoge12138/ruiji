package com.chen.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.R;
import com.chen.dto.DishDto;
import com.chen.pojo.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    R<String> saveDishWithFlavor(DishDto dishDto);

    R<Page> pageByDish(int page, int pageSize, String name);

    R<String> updateDishWithFlavor(DishDto dishDto);

    R<DishDto> getDishInfoById(Long id);

    R<String> deleteDish(List<Long> ids);

    R<String> sellStatus(Integer status, List<Long> ids);

    R<List<DishDto>> getDishInfoByCondition(Dish dish);


}
