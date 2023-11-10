package com.chen.controller;

import com.chen.service.OrderService;
import com.chen.common.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chen.pojo.Orders;
import com.chen.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单管理
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService ordersService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submitOrder(@RequestBody Orders orders) {
        return ordersService.submitOrder(orders);
    }

    /**
     * 订单分页查询
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public R<Page<Orders>> page(
            int page,
            int pageSize,
            String number,
            @DateTimeFormat(pattern = "yyyy-mm-dd HH:mm:ss") Date beginTime,
            @DateTimeFormat(pattern = "yyyy-mm-dd HH:mm:ss") Date endTime) {
        log.info(
                "订单分页查询：page={}，pageSize={}，number={},beginTime={},endTime={}",
                page,
                pageSize,
                number,
                beginTime,
                endTime);
        Page<Orders> ordersPage = ordersService.pageOrders(page, pageSize, number, beginTime, endTime);
        return R.success(ordersPage);
    }


//    @PostMapping("/status/{status}")
//    public R<String> sellStatus(@PathVariable("status") Integer status, @RequestParam List<Long> ids) {
//        return dishService.sellStatus(status, ids);
//    }
    @PutMapping
    public R<String> orderStatusChange(@RequestBody Map<String, String> map) {

        return ordersService.orderStatusChange(map);

    }




}
