package com.example.LicenseManagement.controller;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.LicenseManagement.dto.EncryptedData;
import com.example.LicenseManagement.emailconfig.Email;
import com.example.LicenseManagement.entity.License;
import com.example.LicenseManagement.exception.EncryptedException;
import com.example.LicenseManagement.generate.Generate;
import com.example.LicenseManagement.service.LicenseService;

@RestController
@RequestMapping("/license")
public class LicenseController {

	@Autowired
	private LicenseService licenseService;

	@Autowired
	private Email mailService;
	
	@Autowired
	private Generate generate;

	@PostMapping("/create")
	public ResponseEntity<License> createLicense(@RequestBody License license) {
		try {
			License l1 = licenseService.createCompany(license);
			return ResponseEntity.ok(l1);
		} catch (NoSuchAlgorithmException e) {
			return ResponseEntity.status(500).build();
		}
	}

	@PutMapping("/create/{companyName}")
	public String getValues(@PathVariable("companyName") String companyName) {
		String license = licenseService.generateLicenseKey(companyName);
		return "License Key" + " : " + license;
	}

	@GetMapping("/send/email")
	public String sendMail(@RequestParam String to, @RequestParam String subject, @RequestParam String text) {
		mailService.sendMessage(to, subject, text);
		return "Email sent Successfully";
	}

	@PostMapping("/encryptlicense")
	public ResponseEntity<?>encrypt(@RequestParam String companyName,
			                                    @RequestParam String adminEmail,
			                                     @RequestParam String subject){
		try {
		EncryptedData encryptedData=generate.encryptEmailLicense(companyName,adminEmail,subject);
		return ResponseEntity.ok(encryptedData);
} catch (EncryptedException e) {
	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
}
		
	}}
	
	
	
	

	
	
	
	
	
	
	
	
