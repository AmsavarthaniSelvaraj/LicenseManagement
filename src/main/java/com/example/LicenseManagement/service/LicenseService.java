package com.example.LicenseManagement.service;

import java.security.NoSuchAlgorithmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.LicenseManagement.entity.License;
import com.example.LicenseManagement.enumeration.StatusEnum;
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
		License l = repo1.save(license);
		return l;
	}

	public String generateLicenseKey(String companyName) {
		License license = repo1.findByCompanyName(companyName);
		String company = generate.generateLicenseKey(license.getCompanyName());
		license.setLicenseKey(company);
		license.setStatus(StatusEnum.REQUEST);
		repo1.save(license);
		return company;
	}

}
