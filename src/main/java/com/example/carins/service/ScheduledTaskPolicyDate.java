package com.example.carins.service;

import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.InsurancePolicyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;


@Component
public class ScheduledTaskPolicyDate {
    private static final Logger log =LoggerFactory.getLogger(ScheduledTaskPolicyDate.class);
    private final InsurancePolicyRepository policyRepository;

    public ScheduledTaskPolicyDate(InsurancePolicyRepository policyRepository){
        this.policyRepository=policyRepository;
    }


    @Scheduled(cron = "0 0 1 * * *")
    public void logExpiredPolicies() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<InsurancePolicy> policies=policyRepository.findByEndDate(yesterday);
        if (!policies.isEmpty()) {
            policies.forEach(p -> log.info("Policy {} for car {} expired on {}", p.getId(),p.getCar().getId(), p.getEndDate()));
        }


    }
}
