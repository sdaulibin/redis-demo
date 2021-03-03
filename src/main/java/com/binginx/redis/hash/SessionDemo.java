package com.binginx.redis.hash;

import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 用户回话管理
 */
public class SessionDemo {
    private Jedis jedis = new Jedis("127.0.0.1");

    /**
     * 检查session是否有效
     * @param token
     * @return
     */
    public boolean isSessionValid(String token) throws Exception{
        //校验token是否为空
        if(null == token || "".equals(token)) {
            return false;
        }
        //这里拿到的session可能是个json字符串
        //简化一下，放一个uid作为value
        String session = jedis.hget("sessions", "session::" + token);
        if(null == session || "".equals(session)) {
            return false;
        }
        //检查session是否在有效期内
        String expireTime = jedis.hget("sessions::expire_time", "session::" + token);
        if(null == expireTime || "".equals(expireTime)) {
            return false;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date expireTimeDate = simpleDateFormat.parse(expireTime);
        Date now = new Date();
        if(now.after(expireTimeDate)) {
            return false;
        }
        return true;
    }

    public void initSession(long userId,String token) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR,24);
        Date expireTime = calendar.getTime();
        jedis.hset("sessions","session::"+token,String.valueOf(userId));
        jedis.hset("sessions::expire_time","session::"+token,simpleDateFormat.format(expireTime));
    }
}
