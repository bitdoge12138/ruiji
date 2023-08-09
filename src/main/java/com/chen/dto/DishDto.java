package com.chen.dto;

import com.chen.pojo.Dish;
import com.chen.pojo.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {
    // 由于新建菜品的页涉及到Dish和DishFlavor两个实体类，所以重新封装了一个DishDto类来进行处理

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
