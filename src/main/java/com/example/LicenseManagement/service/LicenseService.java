package com.example.LicenseManagement.service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.LicenseManagement.entity.License;
import com.example.LicenseManagement.generate.Generate;
import com.example.LicenseManagement.repository.LicenseRepository;

import io.micrometer.common.lang.NonNull;

@Service
public class LicenseService {
	@Autowired
	private LicenseRepository repo1;
	
	@Autowired
	private @NonNull Generate generate;
	
	public License addValue(License license) {
		return repo1.save(license);
	}
	
	
	public License createCompany(License license) throws NoSuchAlgorithmException {
		License l=repo1.save(license);
		return l;
	}
//		 String encrytKey;
//		encrytKey=generateLicenseKey(license);
//		license.setLicenseKey(encrytKey);
//		license.setLicenseKey(companyName);
		
	public String generateLicenseKey(String companyName) {
		Optional<License>license= repo1.findByCompanyName(companyName);
		License licenseObj = license.get();
	//	String Company =licenseObj.getCompanyName();
		String company= generate.generateLicenseKey(licenseObj.getCompanyName());
		return company;
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
 

