package com.chen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.pojo.Orders;

public interface OrderService extends IService<Orders> {

    void submit(Orders orders);
}
