package com.example.leave.services.Impl;

import com.example.leave.infrastructure.security.RedisUtil;
import com.example.leave.models.WorkTime;
import com.example.leave.repositories.WorkTimeRepository;
import com.example.leave.services.WorkTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WorkTimeServiceImpl implements WorkTimeService {
    @Autowired
    private WorkTimeRepository workTimeRepository;

    @Value("${app.workTime}")
    private String workTimee;

    @Value("${app.startWorkTime}")
    private String startWorkTime;

    @Value("${app.stopMorningWorkTime}")
    private String stopMorningWorkTime;

    @Value("${app.startAfternoonWorkTime}")
    private String startAfternoonWorkTime;

    @Value("${app.endWorkTime}")
    private String endWorkTime;

    @Override
    public  boolean existCacheRedis(){
        Map<String, String> workTimes =  RedisUtil.INSTANCE.hgetAll(workTimee);
        if(workTimes != null){
            return  false;}
        else return true;
    }

    @Override
    public void pushCacheRedis(){
        WorkTime workTime = workTimeRepository.getWorkTime();
        String valueStartWorkTime = String.valueOf(workTime.getStartWorkTime());
        String valueStopMorningWorkTime = String.valueOf(workTime.getStopMorningWorkTime());
        String valueStartAfternoonWorkTime = String.valueOf(workTime.getStartAfternoonWorkTime());
        String valueEndWorkTime = String.valueOf(workTime.getEndWorkTime());
        RedisUtil.INSTANCE.hset(workTimee, startWorkTime, valueStartWorkTime);
        RedisUtil.INSTANCE.hset(workTimee, stopMorningWorkTime, valueStopMorningWorkTime);
        RedisUtil.INSTANCE.hset(workTimee, startAfternoonWorkTime, valueStartAfternoonWorkTime);
        RedisUtil.INSTANCE.hset(workTimee, endWorkTime, valueEndWorkTime);
    }

    @Override
    public Integer pullCacheRediss(String time) {
        return Integer.parseInt(RedisUtil.INSTANCE.hget(workTimee, time));
    }
}
