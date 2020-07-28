package com.luas.securitylearning.service.impl;

import com.luas.securitylearning.service.SmsService;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    @Override
    public void send() {
        System.out.println("短信发送成功！");
    }
}
