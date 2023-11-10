package com.chen.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.R;
import com.chen.pojo.Orders;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.Map;

public interface OrderService extends IService<Orders> {

    R<String> submitOrder(Orders orders);

    Page<Orders> pageOrders(int page, int pageSize, String number, Date beginTime, Date endTime);

    R<String> orderStatusChange(Map<String, String> map);

}
