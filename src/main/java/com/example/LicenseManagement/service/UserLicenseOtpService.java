package com.example.LicenseManagement.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.LicenseManagement.emailconfig.Email;
import com.example.LicenseManagement.entity.UserLicenseOtp;
import com.example.LicenseManagement.enumeration.OtpEnum;
import com.example.LicenseManagement.enumeration.OtpStatus;
import com.example.LicenseManagement.repository.UserLicenseOtpRepository;

@Service
public class UserLicenseOtpService {
	@Autowired
	private UserLicenseOtpRepository repo1;
	
	@Autowired
	private Email emailService;
	
	
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
		otp.setOtpEnum(OtpEnum.NOT_VERIFED);
		
		UserLicenseOtp savedOtp = repo1.save(otp);
		emailService.sendMessage(email,"Your OTP Code","Your OTP Code is:"+savedOtp.getOtp());
	
		return savedOtp;
	}
	//OTP that the user has provided for verification.(String otpToVerify)
	public boolean verifyOtp(String email,String otpToVerify) {
		Optional<UserLicenseOtp> optional=repo1.findByEmail(email);
		
		if (optional.isPresent()) {
			UserLicenseOtp otp=optional.get();
			
			if(otp.getOtpStatus()==OtpStatus.IN_VALID || otp.getExpiryTime().isBefore(LocalDateTime.now())) {
				return false;
				}
			
			if(otp.getOtp().equals(otpToVerify)) {
				otp.setOtpStatus(OtpStatus.USED);
				otp.setOtpEnum(OtpEnum.VERIFED);
				repo1.save(otp);
				return true;
			}
	}
		return false;
	}}



















