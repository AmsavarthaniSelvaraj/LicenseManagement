package com.example.LicenseManagement.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Base64;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.LicenseManagement.entity.License;
import com.example.LicenseManagement.repository.LicenseRepository;

import Enumeration.ExpiredStatus;
import Enumeration.StatusEnum;

@Service
public class LicenseService {
	@Autowired
	private LicenseRepository repo1;
	
	public String generateLicenseKey(License license) {
		String combinedString=license.getCompanyName()+" . "+license.getCommonEmail()+ " . "+license.getId();
		
		MessageDigest algorithm;
		try {
		algorithm=MessageDigest.getInstance("SHA-256");
		} catch(NoSuchAlgorithmException e) {
			throw new RuntimeException("Exception occured");
		}
		byte[] digest=algorithm.digest(combinedString.getBytes());
		String licenseKey = Base64.getEncoder().encodeToString(digest);
		licenseKey=licenseKey.replaceAll("(.{4})(?!$)","$1-");
		return licenseKey;
	}
	
	public License assignLicenseKey(License license) throws NoSuchAlgorithmException {
		 String encrytKey;
		encrytKey=generateLicenseKey(license);
		license.setLicenseKey(encrytKey);
		return license;
	}
}
//		license.setLicenseKey(combinedString);
//     	license.setActivationDate(LocalDate.now());
//		license.setExpiryDate(LocalDate.now().plusDays(license.getGracePeriod()));
//		license.setStatus(StatusEnum.ACTIVATE);
//		license.setExpiredStatus(ExpiredStatus.NOT_EXPIRED);
//		repo1.save(license);
//		return combinedString;
//	}
//	
 

