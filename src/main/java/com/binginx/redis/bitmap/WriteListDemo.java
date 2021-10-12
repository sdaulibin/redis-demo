package com.binginx.redis.bitmap;

import redis.clients.jedis.Jedis;

public class WriteListDemo {
    private Jedis jedis = new Jedis("127.0.0.1");

    public boolean setWriteList(String bitKey,Long offset,String bitValue) {
        return jedis.setbit(bitKey,offset,bitValue);
    }

    public boolean getWriteList(String bitKey,Long offset) {
        return jedis.getbit(bitKey,offset);
    }

    public static void main(String[] args) {
        long offset = 12345;
        WriteListDemo writeListDemo = new WriteListDemo();
        final boolean set = writeListDemo.setWriteList("bitkey", offset,"0");
        System.out.println("setWriteList:"+set);
        final boolean get = writeListDemo.getWriteList("bitkey", offset);
        System.out.println("getWriteList:"+get);


        System.out.println("getWriteList:"+ writeListDemo.jedis.bitcount("bitkey"));

        writeListDemo.jedis.del("bitkey");
    }
}
