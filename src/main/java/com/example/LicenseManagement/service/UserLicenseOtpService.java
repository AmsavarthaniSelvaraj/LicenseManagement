package com.example.LicenseManagement.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.LicenseManagement.entity.UserLicenseOtp;
import com.example.LicenseManagement.enumeration.OtpStatus;
import com.example.LicenseManagement.repository.UserLicenseOtpRepository;

@Service
public class UserLicenseOtpService {
	@Autowired
	private UserLicenseOtpRepository repo1;
	
	
	private String generateRandomOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generate a 6-digit OTP
        return String.valueOf(otp);
    }
	
	public UserLicenseOtp generateOtp(String email) {
		UserLicenseOtp otp=new UserLicenseOtp();
		otp.setEmail(email);
		otp.setOtp(generateRandomOtp());
		otp.setOtpCreateTime(LocalDateTime.now());
		otp.setExpiryTime(LocalDateTime.now().plusMinutes(5));
		otp.setOtpStatus(OtpStatus.VALID);
		
		return repo1.save(otp);
	}

	
	}


