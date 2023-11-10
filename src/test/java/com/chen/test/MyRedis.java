package com.chen.test;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.Set;

public class MyRedis {


    @Test
    public void TestRedis() {


        Jedis jedis = new Jedis("localhost", 6379);

        jedis.set("username", "bitdoge");


        String value = jedis.get("username");

        System.out.println(value);


        Set<String> keys = jedis.keys("*");


        for (String key: keys) System.out.println(key);


        jedis.close();


    }


}
