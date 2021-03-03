package com.binginx.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.List;

public class JedisTest {
    public static void main(String[] args) {
        //测试
        Jedis jedis = new Jedis("127.0.0.1");
        jedis.set("hello","world");
        System.out.println(jedis.get("hello"));
        //最简单的分布式锁
        Long delResult = jedis.del("lock_test");
        System.out.println("del result:"+delResult);
        String result = jedis.set("lock_test", "value_test", SetParams.setParams().nx());
        System.out.println("第一次加锁结果："+result);
        result = jedis.set("lock_test", "value_test", SetParams.setParams().nx());
        System.out.println("第二次加锁结果："+result);
        //博客发布、修改与查看
        Long msetnxResult = jedis.msetnx("article:1:title", "学习redis", "article:1:content", "如何学好redis的使用",
                "article:1:author", "zhangsan", "article:1:time", "2021-03-01 12:00:00");
        System.out.printf("发布博客的结果：%s\n",msetnxResult);

        List<String> mgetResult = jedis.mget("article:1:title", "article:1:content", "article:1:author", "article:1:time");
        System.out.printf("获取博客信息：%s\n",mgetResult);

        String updateMset = jedis.mset("article:1:title", "修改学习redis", "article:1:content", "修改如何学好redis的使用");
        System.out.printf("修改博客的结果：%s\n",updateMset);

        mgetResult = jedis.mget("article:1:title", "article:1:content", "article:1:author", "article:1:time");
        System.out.printf("再次获取博客信息：%s\n",mgetResult);

        Long strlen = jedis.strlen("article:1:content");
        System.out.printf("博客内容长度：%d\n",strlen);

        String getrange = jedis.getrange("article:1:content", 0, 5);
        System.out.printf("博客预览：%s\n",getrange);

        //操作日志的审计功能
        jedis.set("operation_log_2021-01-01","");
        for (int i = 0; i < 10; i++) {
            jedis.append("operation_log_2021-01-01","今天的第"+i+"条日志\n");
        }
        String appendResult = jedis.get("operation_log_2021-01-01");
        System.out.println(appendResult);

        //唯一id生成
        for (int i = 0; i < 20; i++) {
            Long orderId = jedis.incr("order_id_test");
            System.out.printf("生成的第%d个id：%s\n",i+1,orderId);
        }

        //博客点赞
        for (int i = 0; i < 20; i++) {
            jedis.incr("article:1:dianzan");
        }
        String dianzanCounter = jedis.get("article:1:dianzan");
        System.out.printf("点赞次数：%s\n", dianzanCounter);

        for (int i = 0; i < 5; i++) {
            jedis.decr("article:1:dianzan");
        }
        dianzanCounter = jedis.get("article:1:dianzan");
        System.out.printf("点赞次数%s\n",dianzanCounter);
    }
}
