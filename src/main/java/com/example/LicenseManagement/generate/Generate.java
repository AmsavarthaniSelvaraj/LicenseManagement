package com.example.LicenseManagement.generate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.LicenseManagement.entity.License;
import com.example.LicenseManagement.repository.LicenseRepository;

@Service

public class Generate {

	@Autowired
	private LicenseRepository repo;

	public String generateLicenseKey(String companyName) {
		Optional<License> license = repo.findByCompanyName(companyName);
		License licenseObj = license.get();
		String combinedString = licenseObj.getCompanyName() + " . " + licenseObj.getCommonEmail() + " . "
				+ licenseObj.getId();

		MessageDigest algorithm;
		try {
			algorithm = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Exception occured");
		}
		byte[] digest = algorithm.digest(combinedString.getBytes());
		String licenseKey = Base64.getEncoder().encodeToString(digest);
		licenseKey = licenseKey.replaceAll("(.{4})(?!$)", "$1-");
		return licenseKey;
	}
}
