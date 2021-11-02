package com.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 获取Jedis工具类
 */
public class JedisUtils {

    // 声明Jedis连接池对象
    private static JedisPool jedisPool;

    // 静态代码块，加载配置文件，初始化连接池对象
    static {
        // 加载配置文件
        InputStream is = JedisPool.class.getClassLoader().getResourceAsStream("jedis.properties");
        // 将流加载进Properties集合中
        Properties p = new Properties();
        try {
            p.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 获取参数，设置到JedisPoolConfig中
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.parseInt(p.getProperty("maxTotal")));
        config.setMaxIdle(Integer.parseInt(p.getProperty("maxIdle")));

        // 初始化连接池对象
        jedisPool = new JedisPool(config,p.getProperty("host"),Integer.parseInt(p.getProperty("port")));
    }

    /**
     * 获取连接对象
     * @return
     */
    public static Jedis getJedis(){
        return jedisPool.getResource();
    }
    public static void close(Jedis jedis){
        if (jedis!=null){
            jedis.close();
        }
    }
}
