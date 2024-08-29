package com.example.LicenseManagement.generate;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.LicenseManagement.emailconfig.Email;
import com.example.LicenseManagement.entity.License;
import com.example.LicenseManagement.enumeration.ExpiredStatus;
import com.example.LicenseManagement.enumeration.StatusEnum;
import com.example.LicenseManagement.repository.LicenseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LicenseCheck {
	
	private final LicenseRepository repo;
	
	private final Email emails;
	
	@Scheduled(cron="0 0 8 * * ?")
	public void checkLicenseStatusAndNotify(){
		List<License> license = repo.findAll();
		LocalDate today = LocalDate.now();
		
		for(License l1 : license) {
			LocalDate expiryDate =l1.getExpiryDate();
			LocalDate gracePeriodEnd=l1.getGracePeriod();
			
			if (l1.getStatus() == StatusEnum.APPROVED && expiryDate != null) {
			    long daysUntilExpiry = ChronoUnit.DAYS.between(today, expiryDate);

			    if (daysUntilExpiry > 0 && daysUntilExpiry <= 7) {
			        String subject = "License Expiration Warning";
			        String emailBody = "Your license for " + l1.getCompanyName() +
			                           " is set to expire on " + expiryDate +
			                           ". Please renew your license to avoid service disruption.";
			        emails.sendMessage(l1.getCommonEmail(), subject, emailBody);

			    } else if (today.isAfter(expiryDate) && (gracePeriodEnd == null || today.isBefore(gracePeriodEnd))) {
			        String subject = "License Grace Period Warning";
			        String emailBody = "Your license for " + l1.getCompanyName() +
			                           " is currently in the grace period. The grace period will end on " +
			                           gracePeriodEnd + ". Please renew your license immediately.";
			        emails.sendMessage(l1.getCommonEmail(), subject, emailBody);
			    } else if (gracePeriodEnd != null && today.isAfter(gracePeriodEnd)) {
			    	l1.setStatus(StatusEnum.IN_ACTIVATE);			       
			    	l1.setExpiredStatus(ExpiredStatus.EXPIRED);			    
			    	repo.save(l1);
			    }
			}
	}
			

}}
