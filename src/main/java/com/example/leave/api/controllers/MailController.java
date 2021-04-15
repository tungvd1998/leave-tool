package com.example.leave.api.controllers;

import com.example.leave.models.LeaveApplication;
import com.example.leave.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;


@RestController
public class MailController {

    @Autowired
    private MailService mailService;

    @RequestMapping(value = {"/email/send"}, method = RequestMethod.GET)
    public void sendEmail() {
        LeaveApplication leaveApplication = new LeaveApplication();
        leaveApplication.setReason("Om");
        leaveApplication.setCreated(new Date());
        mailService.sendEmail("b",leaveApplication);
    }

}
