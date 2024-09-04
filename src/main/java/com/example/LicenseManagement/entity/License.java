package com.example.LicenseManagement.entity;

import java.time.LocalDate;
import java.util.UUID;

import com.example.LicenseManagement.enumeration.ExpiredStatus;
import com.example.LicenseManagement.enumeration.OtpEnum;
import com.example.LicenseManagement.enumeration.StatusEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="license")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class License {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	private String companyName;
	private String address;
	private String contactNo;
	private String commonEmail;
	private LocalDate gracePeriod;
	private LocalDate expiryDate;
	@Enumerated(EnumType.STRING)
	private StatusEnum status;
	@Enumerated(EnumType.STRING)
	private ExpiredStatus expiredStatus;
	private LocalDate activationDate;
	private String licenseKey;
	
	
	

}
