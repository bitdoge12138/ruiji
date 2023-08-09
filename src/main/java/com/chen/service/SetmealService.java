package com.chen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.dto.SetmealDto;
import com.chen.pojo.Setmeal;

import java.util.List;


public interface SetmealService extends IService<Setmeal> {

    void saveWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);
}
