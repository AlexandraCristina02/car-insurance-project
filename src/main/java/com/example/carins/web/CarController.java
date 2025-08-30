package com.example.carins.web;

import com.example.carins.model.Car;
import com.example.carins.model.InsuranceClaim;
import com.example.carins.service.CarService;
import com.example.carins.web.dto.CarDto;
import com.example.carins.web.dto.ClaimMapper;
import com.example.carins.web.dto.ClaimRequestDto;
import com.example.carins.web.dto.ClaimResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CarController {

    private final CarService service;
    private final ClaimMapper claimMapper;

    public CarController(CarService service, ClaimMapper claimMapper) {
        this.service = service;
        this.claimMapper = claimMapper;
    }

    @GetMapping("/cars")
    public List<CarDto> getCars() {
        return service.listCars().stream().map(this::toDto).toList();
    }

    @GetMapping("/cars/{carId}/insurance-valid")
    public ResponseEntity<?> isInsuranceValid(@PathVariable Long carId, @RequestParam String date) {
        if (!service.carExists(carId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        LocalDate d ;
        try{
            d= LocalDate.parse(date);
        }catch (DateTimeParseException e) {
            return ResponseEntity.badRequest()
                    .body("Invalid date format. Expected format: YYYY-MM-DD");
        }
        LocalDate minDate = LocalDate.of(1900, 1, 1);
        LocalDate maxDate = LocalDate.of(2100, 12, 31);
        if(d.isBefore(minDate)||d.isAfter(maxDate)){
            return ResponseEntity.badRequest()
                    .body("Date outside supported range " + minDate + " to " + maxDate);
        }
        boolean valid = service.isInsuranceValid(carId, d);
        return ResponseEntity.ok(new InsuranceValidityResponse(carId, d.toString(), valid));
    }

    @PostMapping("/cars/{carId}/claims")
    public ResponseEntity<ClaimResponseDto> createInsuranceClaim(@PathVariable Long carId, @Valid @RequestBody ClaimRequestDto claimRequestDto) {
        if (!service.carExists(carId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        InsuranceClaim newClaim = service.saveInsuranceClaim(carId, claimRequestDto);
        ClaimResponseDto newClaimDto = claimMapper.toDto(newClaim);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newClaimDto.getId())
                .toUri();
        return ResponseEntity.created(location).body(newClaimDto);

    }

    @GetMapping("/cars/{carId}/history")
    public ResponseEntity<List<ClaimResponseDto>> getCarHistory(@PathVariable Long carId) {
        if (!service.carExists(carId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<InsuranceClaim> insuranceClaimList = service.getInsuranceClaimsByCar(carId);
        List<ClaimResponseDto> insuranceClaimListDto = claimMapper.toDtoList(insuranceClaimList);
        return ResponseEntity.ok(insuranceClaimListDto);
    }


    private CarDto toDto(Car c) {
        var o = c.getOwner();
        return new CarDto(c.getId(), c.getVin(), c.getMake(), c.getModel(), c.getYearOfManufacture(),
                o != null ? o.getId() : null,
                o != null ? o.getName() : null,
                o != null ? o.getEmail() : null);
    }

    public record InsuranceValidityResponse(Long carId, String date, boolean valid) {
    }
}
