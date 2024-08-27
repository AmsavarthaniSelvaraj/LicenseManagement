package com.example.LicenseManagement.service;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.example.LicenseManagement.dto.EncryptedData;

@Component
public class SecretKeyGenerator {
	private static final String ALGORITHM = "AES";

	public EncryptedData encrypt(String data, SecretKey key) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encryptedData = cipher.doFinal(data.getBytes("UTF-8"));
			// encoded
			String encryptedDataLicense = Base64.getEncoder().encodeToString(encryptedData);
			String encodedSecretKey = Base64.getEncoder().encodeToString(key.getEncoded());
			return new EncryptedData(encryptedDataLicense, encodedSecretKey);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public  SecretKey generateSecretKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(256); // 256-bit key.key sizes(128, 192, or 256 bits)
		return keyGen.generateKey();

	}
	
	 public String decrypt(String encryptedDataLicense, String encodedSecretKey) {
	        try {
	            // Decode the encrypted data and key
	            byte[] encryptedData = Base64.getDecoder().decode(encryptedDataLicense);
	            byte[] decodedKey = Base64.getDecoder().decode(encodedSecretKey);

	            // Reconstruct the secret key
	            SecretKey key = new javax.crypto.spec.SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);

	            Cipher cipher = Cipher.getInstance(ALGORITHM);
	            cipher.init(Cipher.DECRYPT_MODE, key);
	            byte[] originalData = cipher.doFinal(encryptedData);
	            return new String(originalData, "UTF-8");
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
}
	
	
	
	
	
	
	
	

