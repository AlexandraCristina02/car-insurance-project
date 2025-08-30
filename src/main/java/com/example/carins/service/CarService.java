package com.example.carins.service;

import com.example.carins.model.Car;
import com.example.carins.model.InsuranceClaim;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsuranceClaimRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.web.dto.ClaimRequestDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final InsurancePolicyRepository policyRepository;
    private final InsuranceClaimRepository claimRepository;

    public CarService(CarRepository carRepository, InsurancePolicyRepository policyRepository, InsuranceClaimRepository claimRepository) {
        this.carRepository = carRepository;
        this.policyRepository = policyRepository;
        this.claimRepository=claimRepository;
    }

    public List<Car> listCars() {
        return carRepository.findAll();
    }

    public boolean isInsuranceValid(Long carId, LocalDate date) {
        if (carId == null || date == null) return false;
        // TODO: optionally throw NotFound if car does not exist
        return policyRepository.existsActiveOnDate(carId, date);
    }

    public InsuranceClaim saveInsuranceClaim(Long carId, ClaimRequestDto claimRequestDto){
        Car car=carRepository.findById(carId) .orElseThrow(() -> new RuntimeException("Car not found"));

        InsuranceClaim insuranceClaim= new InsuranceClaim();
        insuranceClaim.setCar(car);
        insuranceClaim.setClaimDate(claimRequestDto.getClaimDate());
        insuranceClaim.setDescription(claimRequestDto.getDescription());
        insuranceClaim.setAmount(claimRequestDto.getAmount());

        return claimRepository.save(insuranceClaim);
    }

    public List<InsuranceClaim> getInsuranceClaimsByCar(Long carId){
        return claimRepository.findByCarIdOrderByClaimDateAsc(carId);
    }

    public boolean carExists(Long carId){
        return carRepository.existsById(carId);
    }




}
