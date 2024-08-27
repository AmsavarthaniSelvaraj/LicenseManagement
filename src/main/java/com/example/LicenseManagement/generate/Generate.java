package com.example.LicenseManagement.generate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.LicenseManagement.dto.EncryptedData;
import com.example.LicenseManagement.dto.Encryption;
import com.example.LicenseManagement.emailconfig.Email;
import com.example.LicenseManagement.entity.License;
import com.example.LicenseManagement.enumeration.StatusEnum;
import com.example.LicenseManagement.exception.EncryptedException;
import com.example.LicenseManagement.repository.LicenseRepository;
import com.example.LicenseManagement.service.SecretKeyGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Generate {

	private final LicenseRepository repo;

	private final SecretKeyGenerator encryptionService;

	private final Email emails;

	public String generateLicenseKey(String companyName) {
		License license = repo.findByCompanyName(companyName);
		String combinedString = license.getCompanyName() + " . " + license.getCommonEmail() + " . " + license.getId();

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
		String secret = "AES";
		SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encryptKey = cipher.doFinal(licenseKey.getBytes());
		return Base64.getEncoder().encodeToString(encryptKey);
	}

	public EncryptedData encryptEmailLicense(String companyName, String adminEmail, String subject) throws EncryptedException {
	    License license = repo.findByCompanyName(companyName);

	    try {
	        // Generate Secret Key
	        SecretKey secretKey = encryptionService.generateSecretKey();

	        // Encrypt license and email
	        EncryptedData encryptedData = encryptionService.encrypt(license.getCommonEmail() + " . " + license.getLicenseKey(), secretKey);

	        // Convert secret key to a string
	        String secretKeyString = Base64.getEncoder().encodeToString(secretKey.getEncoded());

	        // Prepare the email body with license and secret key
	        String emailBody = "Your Encrypted License Key: " + encryptedData.getEncryptedData() +
	                           "\nSecret Key: " + secretKeyString;

	        // Send email
	        emails.sendMessage(adminEmail, subject, emailBody);

	        // Update license status and save
	        license.setStatus(StatusEnum.REQUEST);
	        repo.save(license);

	        return encryptedData;

	    } catch (Exception e) {
	        throw new EncryptedException("Encryption failed for company: " + companyName, e);
	    }
	}
	
	public String decryptForActivate(Encryption encryption) {
	
	    if (encryption == null) {
	        return "Invalid encryption data";
	    }

	    // Find the license directly by email and license
	    License license = repo.findByEmailAndLicense(encryption.getEmail(), encryption.getLicenseKey());

	    // Check if the license is null
	    if (license == null) {
	        return "License not found";
	    }

	    String storedLicenseKey = license.getLicenseKey(); // License key stored in the database

	    // Decrypt the encrypted license key provided
	    String decryptedLicenseKey = encryptionService.decrypt(encryption.getLicenseKey(), encryption.getEmail());

	    // Compare the decrypted license key with the stored license key
	    if (!storedLicenseKey.equals(decryptedLicenseKey)) {
	        return "Decrypted license key does not match the stored license key";
	    }

	    // If the license key matches, proceed with activation
	    LocalDate activationDate = LocalDate.now();
	    license.setActivationDate(activationDate);
        LocalDate expiryDate = activationDate.plusDays(30); // Set expiration date 30 days from activation
        license.setExpiryDate(expiryDate);
        LocalDate gracePeriodStart=expiryDate.plusDays(1);
        LocalDate gracePeriodEnd=gracePeriodStart.plusDays(1);
        license.setGracePeriod(gracePeriodEnd);
	    license.setStatus(StatusEnum.APPROVED);

	    repo.save(license);
	    return "License Activated Successfully";
	} }



























