package com.example.LicenseManagement.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.LicenseManagement.entity.UserLicenseOtp;

@Repository
public interface UserLicenseOtpRepository extends JpaRepository<UserLicenseOtp,UUID>{
//	@Query(value="select*from userLicenseOtp us where us.license=:license",nativeQuery=true)
//	UserLicenseOtp findByLicense(@Param("license") String license);
}
