package com.chen.test;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringDataRedisTest {


    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    public void testString() {
        // 默认的Key序列化器为JdkSerializationRedisSerializer
        redisTemplate.opsForValue().set("city", "beijing");

        String value = (String) redisTemplate.opsForValue().get("city");

        System.out.println(value);



    }






}
