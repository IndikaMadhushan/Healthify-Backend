package com.healthcare.personal_health_monitoring.util;

import java.util.Random;

public class OtpGenerator {
    public static String generateOtp() {
        Random r = new Random();
        //generate 6 digit otp
        return String.valueOf(100000 + r.nextInt(900000));
    }
}
