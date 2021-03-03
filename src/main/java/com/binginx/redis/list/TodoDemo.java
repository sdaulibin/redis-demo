package com.binginx.redis.list;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ListPosition;

import java.util.List;
import java.util.Random;

/**
 * 办公OA示例
 */
public class TodoDemo {
    private Jedis jedis = new Jedis("127.0.0.1");

    /**
     * 添加待办事项
     * @param userId
     * @param todoEvent
     */
    public void addTodoEvent(long userId,String todoEvent) {
        jedis.lpush("todo_event::"+userId,todoEvent);
    }

    /**
     * 分页查询待办事件列表
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<String> findTodoEventByPage(long userId,int pageNo,int pageSize) {
        int startIndex = (pageNo -1) * pageSize;
        int endIndex = pageNo * pageSize -1;
        return jedis.lrange("todo_event::"+userId,startIndex,endIndex);
    }

    /**
     * 插入待办事项
     * @param userId
     * @param position
     * @param todoEvent
     * @param targetTodoEvent
     */
    public void insertTodoEvent(long userId, ListPosition position,String targetTodoEvent,String todoEvent) {
        jedis.linsert("todo_event::"+userId,position,targetTodoEvent,todoEvent);
    }

    /**
     * 修改一个待办事项
     * @param userId
     * @param index
     * @param todoEvent
     */
    public void updateTodoEvent(long userId,long index,String todoEvent) {
        jedis.lset("todo_event::"+userId,index,todoEvent);
    }

    /**
     * 完成一个待办事项
     * @param userId
     * @param todoEvent
     */
    public void finishTodoEvent(long userId,String todoEvent) {
        jedis.lrem("todo_evnet::"+userId,0,todoEvent);
    }

    public static void main(String[] args) {
        TodoDemo todoDemo = new TodoDemo();
        long userId = new Random().nextInt(1000);
        for (int i = 0; i < 30; i++) {
            todoDemo.addTodoEvent(userId,"第"+i+"个待办事项");
        }
        int pageNo = 1;
        int pageSize = 10;
        List<String> todoEventByPage = todoDemo.findTodoEventByPage(userId, pageNo, pageSize);
        System.out.println(todoEventByPage);

        String targetTodoEvent = todoEventByPage.get(new Random().nextInt(todoEventByPage.size()));
        todoDemo.insertTodoEvent(userId,ListPosition.BEFORE,targetTodoEvent,"插入待办事项");

        todoEventByPage = todoDemo.findTodoEventByPage(userId, pageNo, pageSize);
        System.out.println(todoEventByPage);

        todoDemo.finishTodoEvent(userId,todoEventByPage.get(0));

        todoDemo.updateTodoEvent(userId,new Random().nextInt(10),"修改待办事项");

        todoEventByPage = todoDemo.findTodoEventByPage(userId, pageNo, pageSize);
        System.out.println(todoEventByPage);
    }
}
