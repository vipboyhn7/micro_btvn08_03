package com.example.appointmentservice.controller;

import com.example.appointmentservice.service.DoctorService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/search")
    @RateLimiter(name = "searchDoctorLimit")
    public ResponseEntity<Object> searchDoctors(@RequestParam String name) {
        return ResponseEntity.ok(doctorService.searchDoctors(name));
    }
}
