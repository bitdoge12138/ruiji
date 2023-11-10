package com.chen.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.R;
import com.chen.pojo.OrderDetail;

public interface OrderDetailService extends IService<OrderDetail> {

    R<Page> pageOrderDetail(int page, int pageSize);

}
