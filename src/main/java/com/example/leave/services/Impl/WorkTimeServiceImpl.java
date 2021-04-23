package com.example.leave.services.Impl;

import com.example.leave.models.WorkTime;
import com.example.leave.repositories.WorkTimeRepository;
import com.example.leave.services.WorkTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WorkTimeServiceImpl implements WorkTimeService {

    @Autowired
    private WorkTimeRepository workTimeRepository;

    private RedisTemplate<String, WorkTime> redisTemplate;

    private HashOperations hashOperations;

    @Value("${app.workTime}")
    private String workTimee;

    public WorkTimeServiceImpl(RedisTemplate<String, WorkTime> redisTemplates) {
        this.redisTemplate = redisTemplates;
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public boolean existCacheRedis() {
        Map<String, WorkTime> workTimes = hashOperations.entries(workTimee);
        if (workTimes != null) {
            return false;
        } else return true;
    }

    @Override
    public void pushCacheRedis() {
        WorkTime workTime = workTimeRepository.getWorkTime();
        String id = String.valueOf(workTime.getId());
        hashOperations.put(workTimee, id, workTime);
    }

    @Override
    public WorkTime pullCacheRedis(String id) {
        return (WorkTime) hashOperations.get(workTimee, id);
    }
}
