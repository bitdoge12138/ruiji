package com.chen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.R;
import com.chen.pojo.ShoppingCart;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {

    R<ShoppingCart> subDishOrSetmeal(ShoppingCart shoppingCart);

    R<ShoppingCart> addDishOrSetmeal(ShoppingCart shoppingCart);

    R<String> cleanShoppingCart();

    R<List<ShoppingCart>> selectShoppingCart();

}
