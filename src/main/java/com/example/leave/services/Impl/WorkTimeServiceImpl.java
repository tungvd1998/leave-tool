package com.example.leave.services.Impl;

import com.example.leave.models.WorkTime;
import com.example.leave.repositories.WorkTimeRepository;
import com.example.leave.services.WorkTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WorkTimeServiceImpl implements WorkTimeService {
    @Autowired
    private WorkTimeRepository workTimeRepository;

    @Autowired
    private RedisTemplate template;

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
    public boolean existCacheRedis() {
        Map<String, String> workTimes = template.opsForHash().entries(workTimee);
        if (workTimes != null) {
            return false;
        } else return true;
    }

    @Override
    public void pushCacheRedis() {
        WorkTime workTime = workTimeRepository.getWorkTime();
        String valueStartWorkTime = String.valueOf(workTime.getStartWorkTime());
        String valueStopMorningWorkTime = String.valueOf(workTime.getStopMorningWorkTime());
        String valueStartAfternoonWorkTime = String.valueOf(workTime.getStartAfternoonWorkTime());
        String valueEndWorkTime = String.valueOf(workTime.getEndWorkTime());
        template.opsForHash().put(workTimee, startWorkTime, valueStartWorkTime);
        template.opsForHash().put(workTimee, stopMorningWorkTime, valueStopMorningWorkTime);
        template.opsForHash().put(workTimee, startAfternoonWorkTime, valueStartAfternoonWorkTime);
        template.opsForHash().put(workTimee, endWorkTime, valueEndWorkTime);
    }

    @Override
    public Integer pullCacheRedis(String time) {
        String number = (String) template.opsForHash().get(workTimee, time);
        return Integer.parseInt(number);
    }
}
