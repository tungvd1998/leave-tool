package com.example.leave.services;

public interface WorkTimeService {
    void pushCacheRedis();
    Integer pullCacheRedis(String time);
    boolean existCacheRedis();
}
