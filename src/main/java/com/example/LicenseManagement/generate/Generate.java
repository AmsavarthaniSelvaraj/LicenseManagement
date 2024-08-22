package com.example.LicenseManagement.generate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.LicenseManagement.entity.License;
import com.example.LicenseManagement.repository.LicenseRepository;

@Service

public class Generate {

	@Autowired
	private LicenseRepository repo;
	
	

	public String generateLicenseKey(String companyName) {
		License license = repo.findByCompanyName(companyName);		
		String combinedString = license.getCompanyName() + " . " + license.getCommonEmail() + " . "
				+ license.getId();

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
	@SuppressWarnings("unused")
	private String encryptLicenseKey(String licenseKey) throws Exception {
		String secret="AES";
		SecretKeySpec secretKey=new SecretKeySpec(secret.getBytes(),"AES");
		Cipher cipher=Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE,secretKey);
		byte[] encryptKey=cipher.doFinal(licenseKey.getBytes());
		return Base64.getEncoder().encodeToString(encryptKey);
	}
	
}
