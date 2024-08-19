package com.example.LicenseManagement.controller;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.LicenseManagement.entity.License;
import com.example.LicenseManagement.service.LicenseService;

		@RestController
		@RequestMapping("/api/licenses")
		public class LicenseController {

		    @Autowired
		    private LicenseService licenseService;

		    @PostMapping("/create")
		    public ResponseEntity<License> createLicense(@RequestBody License license) {
		        try {
		            License updatedLicense = licenseService.assignLicenseKey(license);
		            return ResponseEntity.ok(updatedLicense);
		        } catch (NoSuchAlgorithmException e) {
		            return ResponseEntity.status(500).build();
		        }
		    }
		}

