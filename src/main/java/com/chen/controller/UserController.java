package com.chen.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chen.common.R;
import com.chen.pojo.User;
import com.chen.service.UserService;
import com.chen.utils.SMSUtils;
import com.chen.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {



    @Autowired
    private UserService userService;



    @Autowired
    private RedisTemplate redisTemplate;



    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {

        String phone = user.getPhone();
        log.info("phone={}", phone);

        if (StringUtils.isNotEmpty(phone)) {
            String code = ValidateCodeUtils.generateValidateCode4String(4);
            log.info("code={}", code);

             // 调用阿里云提供的短信服务API完成短信发送
             // SMSUtils.sendMessage("阿里云短信测试", "SMS_154950909", phone, code);


            // 将生成的验证码保存到session中
            // session.setAttribute(phone, code);

            // 将生成的验证码缓存到redis中，并且设置有效期为5分钟
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);


            return R.success("短信发送成功");
        }
        return R.error("短信发送失败");
    }


    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());
        // 获取手机号
        String phone = map.get("phone").toString();

        log.info("phone={}", phone);

        // 获取验证码
        String code = map.get("code").toString();

        log.info("code={}", code);

        // 从session中获取保存的验证码
        // Object codeInSession = session.getAttribute(phone);

        // 从redis中获取保存的验证码
        String codeInSession = (String) redisTemplate.opsForValue().get(phone);

        log.info("codeInSession={}", codeInSession);

        // 验证码比对(页面提交的验证码和session中保存的验证码)
        if (codeInSession != null && codeInSession.equals(code)) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);

            User user = userService.getOne(queryWrapper);


            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }

            session.setAttribute("user", user.getId());

            // 如果登录成功，则删除redis中的验证码信息
            redisTemplate.delete(phone);

            return R.success(user);

        }


        return R.error("登录失败");
    }

}
