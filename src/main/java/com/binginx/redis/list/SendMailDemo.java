package com.binginx.redis.list;

import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * 发送邮件示例
 */
public class SendMailDemo {
    private Jedis jedis = new Jedis("127.0.0.1");

    /**
     * 任务入队
     * @param sendMailTask
     */
    public void enqueueSendMailTask(String sendMailTask) {
        jedis.lpush("send_mail_task_queue",sendMailTask);
    }

    /**
     * 任务阻塞出队
     * @return
     */
    public List<String> takeSendMailTask() {
        return jedis.brpop(5,"send_mail_task_queue");
    }

    public static void main(String[] args) {
        SendMailDemo sendMailDemo = new SendMailDemo();
        List<String> mailTask = sendMailDemo.takeSendMailTask();

        sendMailDemo.enqueueSendMailTask("一个邮件发送任务");

        mailTask = sendMailDemo.takeSendMailTask();
        System.out.println(mailTask);
    }
}
