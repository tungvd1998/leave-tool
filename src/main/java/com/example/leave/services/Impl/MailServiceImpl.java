package com.example.leave.services.Impl;

import com.example.leave.models.LeaveApplication;
import com.example.leave.models.LeavePolicy;
import com.example.leave.models.User;
import com.example.leave.models.WorkTime;
import com.example.leave.repositories.LeaveApplicationRepository;
import com.example.leave.repositories.LeavePolicyRepository;
import com.example.leave.repositories.UserRepository;
import com.example.leave.repositories.WorkTimeRepository;
import com.example.leave.services.MailService;
import com.example.leave.services.WorkTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private LeavePolicyRepository leavePolicyRepository;

    @Autowired
    private WorkTimeRepository workTimeRepository;

    @Autowired
    public JavaMailSender emailSender;
    @Override
    public String sendEmail(String username, LeaveApplication leaveApp) {
        User userLeader = userRepository.getUserLeader(username);
        User user = userRepository.getUser(username);
        LeavePolicy leavePol = leavePolicyRepository.getLeavePolicy(1);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userLeader.getEmail());
        message.setSubject("Notice the employee's resignation");
        message.setText("Dear "+ userLeader.getFirstName() +" "+ userLeader.getLastName() + "," + "\n" +
                "\n" +
                "My name: "+ user.getFirstName() + " "+ user.getLastName()+ "\n" +
                "Leave Policy(type): " + leavePol.getName() +  "\n" +
                "Reason: "+ leaveApp.getReason()+ "\n" +
                "From: " + leaveApp.getFromDate() +  " To "+ leaveApp.getToDate() + "\n" +
                "\n" +
                "I look forward to receiving your response! Thank you very much \n" +
                "\n" +
                "Yours sincerely,\n" +
                user.getFirstName() + " " + user.getLastName());
        // Send Message!
        this.emailSender.send(message);


        return "Email sent";

    }
}
