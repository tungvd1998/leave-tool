package com.example.leave.services;

import com.example.leave.models.WorkTime;

public interface WorkTimeService {
    void pushCacheRedis();
    WorkTime pullCacheRedis(String time);
    boolean existCacheRedis();
}
