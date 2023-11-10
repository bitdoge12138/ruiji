package com.chen.dto;

import com.chen.pojo.Setmeal;
import com.chen.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDishDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
