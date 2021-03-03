package com.binginx.redis.list;

import redis.clients.jedis.Jedis;

/**
 * 秒杀demo
 */
public class SecKillDemo {
    private Jedis jedis = new Jedis("127.0.0.1");

    /**
     * 秒杀请求入队
     * @param secKillRequest
     */
    public void enqueueSecKillRequest(String secKillRequest) {
        jedis.lpush("sec_kill_request_queue",secKillRequest);
    }

    /**
     * 秒杀请求出队
     * @return
     */
    public String dequeueSecKillRequest() {
        return jedis.rpop("sec_kill_request_queue");
    }

    public static void main(String[] args) {
        SecKillDemo secKillDemo = new SecKillDemo();
        for (int i = 1; i <= 10; i++) {
            secKillDemo.enqueueSecKillRequest("第"+i+"个秒杀请求");
        }
        while (true) {
            String secKillRequest = secKillDemo.dequeueSecKillRequest();
            if(null == secKillRequest || "null".equals(secKillRequest) || "".equals(secKillRequest)) {
                break;
            }
            System.out.println(secKillRequest);
        }
    }
}
