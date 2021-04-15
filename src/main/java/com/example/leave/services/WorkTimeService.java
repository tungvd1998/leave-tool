package com.example.leave.services;

public interface WorkTimeService {
    void pushCacheRedis();
    Integer pullCacheRediss(String time);
    boolean existCacheRedis();
}
