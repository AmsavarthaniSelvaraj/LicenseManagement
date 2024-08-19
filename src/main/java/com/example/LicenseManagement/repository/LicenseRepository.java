package com.example.LicenseManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.LicenseManagement.entity.License;

@Repository
public interface LicenseRepository extends JpaRepository<License, Integer>{

}
