package com.binginx.redis.set;

import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 网站访问次数demo
 */
public class UvDemo {
    private Jedis jedis = new Jedis("127.0.0.1");

    /**
     * 添加一次用户访问记录
     * @param userId
     */
    public void addUserAccess(long userId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = simpleDateFormat.format(new Date());
        jedis.sadd("uv::"+today,String.valueOf(userId));
    }

    /**
     * 获取当天的uv
     * @return
     */
    public long getUv() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = simpleDateFormat.format(new Date());
        return jedis.scard("uv::"+today);
    }

    public static void main(String[] args) {
        UvDemo uvDemo = new UvDemo();
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 10; j++) {
                uvDemo.addUserAccess(i+1);
            }
        }
        System.out.println(uvDemo.getUv());
    }
}
