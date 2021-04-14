package com.example.leave.services;

import com.example.leave.models.LeaveApplication;

public interface MailService {
    String sendEmail(String username, LeaveApplication leaveApp);
}
