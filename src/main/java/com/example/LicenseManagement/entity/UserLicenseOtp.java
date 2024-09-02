package com.example.LicenseManagement.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.LicenseManagement.enumeration.OtpStatus;

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
@Table(name="user_license_otp")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLicenseOtp {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	private String otp;
	private String email;
	private LocalDateTime otpCreateTime;
	private LocalDateTime expiryTime;
	@Enumerated(EnumType.STRING)
	private OtpStatus otpStatus;
	

}
