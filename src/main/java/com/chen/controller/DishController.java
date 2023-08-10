package com.chen.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.common.R;
import com.chen.dto.DishDto;
import com.chen.pojo.Category;
import com.chen.pojo.Dish;
import com.chen.pojo.DishFlavor;
import com.chen.service.CategoryService;
import com.chen.service.DishFlavorService;
import com.chen.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;


    @Autowired
    private RedisTemplate redisTemplate;



    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {

        log.info("接收的dishDto数据:{}", dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        // 清理某个分类下面的菜品缓存数据
        String key = "dish_" + dishDto.getCategoryId() + "_1";

        redisTemplate.delete(key);


        return R.success("新增菜品成功");
    }



    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        // 构造分页构造器对象
        Page<Dish> pageInfo = new Page<>();
        Page<DishDto> dishDtoPage = new Page<>();

        // 条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        // 添加条件
        queryWrapper.like(name != null, Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        // 执行分页查询
        dishService.page(pageInfo, queryWrapper);


        // 由于pageInfo对象中没有前端页面的需要的categoryName属性,records变量中存放所有的记录信息
        // 因此新建一个dishDtoPage对象替代pageInfo来进行分页查询
        // 首先将pageInfo中除了records记录外的信息拷贝给dishDtoPage,因为除了records里面要进行处理的categoryName外其它属性不变
        // 接着从pagInfo中将records记录取出，利用stream流的方法先将内容复制赋值给dishDto,接着根据每一条item的categoryId查询categoryName
        // 最后将处理好的DishDto列表设置回dishDtoPage
        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        // 获取原record对象
        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }


            return dishDto;

        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);


        return R.success(dishDtoPage);

    }



    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }



    @PutMapping
    public R<String> update(DishDto dishDto) {

        log.info("接收的dishDto数据:{}", dishDto.toString());

        dishService.updateWithFlavor(dishDto);

        // 清除所有菜品的缓存数据
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);

        return R.success("修改菜品成功");

    }



    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){

        List<DishDto> dishDtoList = null;
        // 动态构造key
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();

        // 先从redis中获取缓存数据
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);


        if (dishDtoList != null) {
            // 如果redis中存在直接返回，无需查询数据库
            return R.success(dishDtoList);
        }


        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //添加条件，查询状态为1（1为起售，0为停售）的菜品
        queryWrapper.eq(Dish::getStatus,1);

        List<Dish> list = dishService.list(queryWrapper);

        dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            //对象拷贝（每一个list数据）
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();  //分类id
            //通过categoryId查询到category内容
            Category category = categoryService.getById(categoryId);
            //判空
            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //获取当前菜品id
            Long dishId = item.getId();

            //构造条件构造器
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper= new LambdaQueryWrapper<>();
            //添加查询条件
            dishFlavorLambdaQueryWrapper.eq(dishId != null,DishFlavor::getDishId,dishId);
            //select * from dish_flavors where dish_id = ?
            List<DishFlavor> dishFlavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);

            dishDto.setFlavors(dishFlavors);

            return dishDto;
        }).collect(Collectors.toList());



        // 如果不存在，需要查询数据库，将查询到的菜品数据缓存到Redis中

        redisTemplate.opsForValue().set(key, dishDtoList, 60, TimeUnit.MINUTES);

        return R.success(dishDtoList);
    }






}
