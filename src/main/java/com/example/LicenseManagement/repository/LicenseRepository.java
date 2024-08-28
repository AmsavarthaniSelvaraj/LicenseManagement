package com.example.LicenseManagement.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.LicenseManagement.entity.License;

@Repository
public interface LicenseRepository extends JpaRepository<License,UUID>{
@Query(value="select*from License l where l.company_name=:companyName",nativeQuery=true)
License findByCompanyName(@Param("companyName")String companyName);

@Query("SELECT l FROM License l WHERE l.commonEmail = :email AND l.licenseKey = :licenseKey")
License findByEmailAndLicense(String email, String licenseKey);
}