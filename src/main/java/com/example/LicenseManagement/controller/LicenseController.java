package com.example.LicenseManagement.controller;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.LicenseManagement.entity.License;
import com.example.LicenseManagement.service.LicenseService;

@RestController
@RequestMapping("/license")
public class LicenseController {

	@Autowired
	private LicenseService licenseService;

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
		 String license=licenseService.generateLicenseKey(companyName);
		 return "License Key" + " : " + license;
	}
}
