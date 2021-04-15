package com.example.leave.infrastructure.security;

import redis.clients.jedis.BuilderFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;

public enum RedisUtil {
    INSTANCE;

    private final JedisPool pool;

    RedisUtil() {
        pool = new JedisPool(new JedisPoolConfig(), "localhost");
    }

    public void sadd(String key, String value) {
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            jedis.sadd(key, value);
            System.out.println(key +"====="+value );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void srem(String key, String value) {
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            jedis.srem(key, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    public Long hset(String name, String key, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.hset(name, key, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    public String hget(String name, String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.hget(name, key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    public Long hdel(String name, String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.hdel(name, key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    public Map<String,String> hgetAll(String key){
    Jedis jedis = null;
        try{
        jedis = pool.getResource();
        return jedis.hgetAll(key);
    } finally {
        if (jedis != null) {
            jedis.close();
        }
    }


    }

}