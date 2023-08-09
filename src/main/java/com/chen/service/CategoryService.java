package com.chen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.pojo.Category;

public interface CategoryService extends IService<Category> {

    void remove(Long id);
}
