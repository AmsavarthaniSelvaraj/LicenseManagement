package com.example.LicenseManagement.generate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.LicenseManagement.dto.EncryptedData;
import com.example.LicenseManagement.dto.Encryption;
import com.example.LicenseManagement.emailconfig.Email;
import com.example.LicenseManagement.entity.License;
import com.example.LicenseManagement.enumeration.ExpiredStatus;
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

	public EncryptedData encryptEmailLicense(String companyName, String adminEmail, String subject)
			throws EncryptedException {
		License license = repo.findByCompanyName(companyName);

		try {
			// Generate Secret Key
			SecretKey secretKey = encryptionService.generateSecretKey();

			// Encrypt license and email
			EncryptedData encryptedData = encryptionService
					.encrypt(license.getCommonEmail() + " . " + license.getLicenseKey(), secretKey);

			// Convert secret key to a string
			String secretKeyString = Base64.getEncoder().encodeToString(secretKey.getEncoded());

			// Prepare the email body with license and secret key
			String emailBody = "Your Encrypted License Key: " + encryptedData.getEncryptedData() + "\nSecret Key: "
					+ secretKeyString;

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

	public String decryptForActivate(EncryptedData encryptedData) {
		if (encryptedData == null) {
			return "Encrypted Data Not Found";
		}

		try {
			// Decrypt the data
			String decryptedData = encryptionService.decrypt(encryptedData);
			System.out.println("Decrypted Data: " + decryptedData);

			if (decryptedData == null) {
				return "Failed to decrypt data.";
			}

			// Adjust the split operation based on the actual format
			String[] parts = decryptedData.split(" \\.");
			if (parts.length != 2) {
				System.out.println("Decrypted Data Format Issue: " + decryptedData);
				return "Invalid decrypted data format. Expected format: email,licenseKey";
			}

			String email = parts[0].trim();
			String licenseKey = parts[1].trim();

			// Find the license using the decrypted licenseKey and email
			License license = repo.findByEmailAndLicense(email, licenseKey);

			if (license != null) {
				// Check if the decrypted licenseKey and email match the one in the database
				if (email.equals(license.getCommonEmail()) && licenseKey.equals(license.getLicenseKey())) {
					LocalDate activationDate = LocalDate.now();
					license.setActivationDate(activationDate);
					LocalDate expiryDate = activationDate.plusDays(30); // Set expiration date 30 days from activation
					license.setExpiryDate(expiryDate);
					LocalDate gracePeriodStart = expiryDate.plusDays(1);
					LocalDate gracePeriodEnd = gracePeriodStart.plusDays(1);
					license.setGracePeriod(gracePeriodEnd);
					license.setStatus(StatusEnum.APPROVED);
					license.setExpiredStatus(ExpiredStatus.NOT_EXPIRED);

					repo.save(license);
					return "Data decrypted successfully and approved. Activation date and expiration date set.";
				} else {
					return "Decrypted license data does not match with the database record.";
				}
			} else {
				return "License or email not found in the database.";
			}
		} catch (Exception e) {
			e.printStackTrace(); // Print the full exception for debugging
			return "Failed to decrypt data.";
		}
	}
}
