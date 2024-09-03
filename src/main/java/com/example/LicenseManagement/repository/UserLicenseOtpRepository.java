package com.example.LicenseManagement.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.LicenseManagement.entity.UserLicenseOtp;

@Repository
public interface UserLicenseOtpRepository extends JpaRepository<UserLicenseOtp,UUID>{
	
    Optional<UserLicenseOtp>findByEmail(@Param("email") String email);

}
