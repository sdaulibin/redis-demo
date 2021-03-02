package com.binginx.redis.hash;

import redis.clients.jedis.Jedis;

/**
 * 短连接生成以及访问案例
 */
public class ShortUrlDemo {

    public static final String[] X36_ARRAY = "0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",");

    private Jedis jedis = new Jedis("127.0.0.1");

    public ShortUrlDemo() {
        jedis.set("short_url_seed","2342424");
    }
    /**
     * 获取短链接网址
     * @param url
     * @return
     */
    public String getShortUrl(String url) {
        long shorUrlSeed = jedis.incr("short_url_seed");
        StringBuffer buffer = new StringBuffer();
        if(shorUrlSeed == 0) {
            buffer.append("0");
        }
        while (shorUrlSeed > 0) {
            buffer.append(X36_ARRAY[(int)(shorUrlSeed % 36)]);
            shorUrlSeed = shorUrlSeed / 36;
        }
        String shortUrl = buffer.reverse().toString();

        jedis.hset("short_url_access_count",shortUrl,"0");
        jedis.hset("url_mapping",shortUrl,url);

        return shortUrl;
    }

    /**
     * 给段连接地址进行访问次数的增长
     * @param shortUrl
     */
    public void incrShortUrlAccessCount(String shortUrl){
        jedis.hincrBy("short_url_access_count",shortUrl,1);
    }

    /**
     * 获取短链接的访问次数
     * @param shortUrl
     * @return
     */
    public long getShortUrlAccessCount(String shortUrl) {
        return Long.valueOf(jedis.hget("short_url_access_count",shortUrl));
    }

    public static void main(String[] args) throws Exception{
        ShortUrlDemo shortUrlDemo = new ShortUrlDemo();
        String shortUrl = shortUrlDemo.getShortUrl("http://www.google.com/baidu/sohu");
        System.out.printf("生成的短连接：%s\n",shortUrl);
        for (int i = 0; i < 159; i++) {
            shortUrlDemo.incrShortUrlAccessCount(shortUrl);
        }
        long shortUrlAccessCount = shortUrlDemo.getShortUrlAccessCount(shortUrl);
        System.out.printf("生成的短连接访问次数：%d\n",shortUrlAccessCount);
    }
}
