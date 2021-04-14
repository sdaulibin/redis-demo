package com.binginx.redis.simulation;

import com.binginx.redis.model.User;
import com.binginx.redis.utils.JsonUtil;
import redis.clients.jedis.Jedis;

import java.util.*;

public class UserDemo {
//    private Jedis jedis = new Jedis("127.0.0.1");

    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1");
        // 存放user的map，user的id为key user的json字符串为value
        Map<String, String> map = new HashMap<String, String>();

        String id1 = UUID.randomUUID().toString();
        User user1 = new User(id1, "张三", 22 ,"m");
        map.put(id1, JsonUtil.toJSONString(user1));

        String id2 = UUID.randomUUID().toString();
        User user2 = new User(id2, "李四", 23 ,"m");
        map.put(id2, JsonUtil.toJSONString(user2));

        String id3 = UUID.randomUUID().toString();
        User user3 = new User(id3, "王五", 26 ,"w");
        map.put(id3, JsonUtil.toJSONString(user3));

        String id4 = UUID.randomUUID().toString();
        User user4 = new User(id4, "王六", 25 ,"w");
        map.put(id4, JsonUtil.toJSONString(user4));

        // 将存有user的map存入redis  key为user
        jedis.hmset("user", map);
        // 测试取出id3的user的信息
        String u = jedis.hget("user", id3);
        // 取出key为user的所有的用户的map，即刚刚存入的map
        Map<String, String> map1 = jedis.hgetAll("user");

//      for (Entry<String, String> entry : map1.entrySet()) {
//           System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
//      }

        // 到此可以查询所有的user也可以根据user的id查询查询某个用户
        // 但是redis不能像关系型数据库似的进行条件查询，
        // select * from user where age between 20 and 25 and sex m; 查询年龄在20到25岁之间的男性
        // redis虽然不像关系型数据库似的对条件查询那样灵活，但也可以利用 hashmap、set sortedset等方式进行简单的条件查询
        // 必须把提前要查询的条件设置好
        // 例如上面user例子 ，可以把年龄和id放入 sortedset中，把性别放入分为w和m放入到set中

        // key = user_age 为sortedset的key值，年龄 为 score； id为member
        String user_age = "user_age";
        jedis.zadd(user_age, 22, id1);
        jedis.zadd(user_age, 23, id2);
        jedis.zadd(user_age, 26, id3);
        jedis.zadd(user_age, 25, id4);
        // 查询20到25之间的id的值
        Set<String> ageSet = jedis.zrangeByScore(user_age, 20, 25);
        // 取出年龄在20到25之间的所有的user
        List<String> hmget = jedis.hmget("user",  (String[])ageSet.toArray(new String[ageSet.size()]));
        for (String string : hmget) {
            User user = (User) JsonUtil.parseObject(string, User.class);
            System.out.println("年龄20到25之间user名字："+user.getName());
        }

        // user_sex_m 为男性set的key值的值，id为member
        String user_sex_m = "user_sex_m";
        jedis.sadd(user_sex_m, id1, id2);

        // user_sex_w 为女性set的key值的值，id为member
        String user_sex_w = "user_sex_w";
        jedis.sadd(user_sex_w, id3);
        jedis.sadd(user_sex_w, id4);

        // 取所有的女性的id
        Set<String> user_sex_wStr = jedis.smembers(user_sex_w);

        // 取所有的女性的user
        List<String>  hh = jedis.hmget("user",  (String[])user_sex_wStr.toArray(new String[user_sex_wStr.size()]));
        for (String string : hh) {
            User user = (User) JsonUtil.parseObject(string, User.class);
            System.out.println("女性的名字："+user.getName());
        }
    }


}
