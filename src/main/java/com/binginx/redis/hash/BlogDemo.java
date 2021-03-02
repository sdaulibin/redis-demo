package com.binginx.redis.hash;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 博客网站案例
 */
public class BlogDemo {
    private Jedis jedis = new Jedis("127.0.0.1");

    /**
     * 获取blog ID
     * @return
     */
    public long getBlogId() {
        return jedis.incr("blog_id_counter");
    }
    /**
     * 发布博客
     * @param blog
     * @param id
     */
    public boolean publishBlog(long id,Map<String,String> blog) {
        if(jedis.hexists("article::"+id,"title")) {
            return false;
        }
        blog.put("content_length",String.valueOf(blog.get("content").length()));
        jedis.hmset("article::"+id,blog);


        /*jedis.msetnx("article:"+id+":title", title,
                "article:"+id+":content", content,
                "article:"+id+":author", author,
                "article:"+id+":time", time);
        long blogLen = jedis.strlen("article:" + id + ":content");
        jedis.setnx("article:"+id+":content_length",String.valueOf(blogLen));*/
        return true;
    }

    /**
     * 查看blog
     * @param id
     * @return
     */
    public Map<String,String> getBlog(long id) {
        Map<String, String> blog = jedis.hgetAll("article::" + id);
        /*List<String> blogInfo = jedis.mget("article:"+id+":title",
                "article:"+id+":content",
                "article:"+id+":author",
                "article:"+id+":time",
                "article:"+id+":content_length",
                "article:"+id+":like_count",
                "article:"+id+":view_count");*/
        viewBlog(id);
        return blog;
    }

    /**
     * 更新blog
     * @param id
     * @param updatedBlog
     */
    public void updateBlog(long id,Map<String,String> updatedBlog) {
        String blogContent = updatedBlog.get("content");
        if(null != blogContent && !"".equals(blogContent)) {
            updatedBlog.put("content_length",String.valueOf(blogContent.length()));
        }
        jedis.hmset("article::"+id,updatedBlog);
    }

    /**
     * 预览blog
     * @param id
     * @return
     */
    /*public String previewBlog(long id) {
        return jedis.getrange("article:"+id+":content", 0, 10);
    }*/

    /**
     * 增加blog点赞次数
     * @param id
     */
    public void likeBlog(long id) {
//        jedis.incr("article:"+id+":like_count");
        jedis.hincrBy("article::"+id,"like_count",1);
    }

    /**
     * 增加blog浏览次数
     * @param id
     */
    public void viewBlog(long id) {
//        jedis.incr("article:"+id+":view_count");
        jedis.hincrBy("article::"+id,"view_count",1);
    }

    public static void main(String[] args) {
        BlogDemo blogDemo = new BlogDemo();
        long id = blogDemo.getBlogId();
        String title = "学习Redis";
        String content = "学习Redis试衣间非常快乐的事情，你是不是非常喜欢？";
        String author = "binginx";
        String time = "2021-03-21 10:00:00";

        Map<String,String> blog = new HashMap<String,String>();
        blog.put("id",String.valueOf(id));
        blog.put("title",title);
        blog.put("content",content);
        blog.put("author",author);
        blog.put("time",time);
        blog.put("content_length",String.valueOf(content.length()));

        blogDemo.publishBlog(id,blog);
        System.out.printf("发布后的blog：%s\n",blogDemo.getBlog(id));
        title = "更新后"+title;
        content = "更新后"+content;
        blog.put("title",title);
        blog.put("content",content);
        blogDemo.updateBlog(id,blog);
//        System.out.printf("有人预览blog%s\n",blogDemo.previewBlog(id));
        System.out.printf("有人浏览blog%s\n",blogDemo.getBlog(id));
        blogDemo.likeBlog(id);
        System.out.printf("我自己浏览blog：%s\n",blogDemo.getBlog(id));
    }
}
