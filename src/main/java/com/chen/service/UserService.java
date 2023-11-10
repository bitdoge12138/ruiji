package com.chen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.common.R;
import com.chen.pojo.User;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface UserService extends IService<User> {

    R<String> sendMsg(User user, HttpSession session);

    R<User> login(Map map, HttpSession session);
}
