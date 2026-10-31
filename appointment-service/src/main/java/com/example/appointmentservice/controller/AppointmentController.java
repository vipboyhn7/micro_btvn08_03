package com.example.appointmentservice.controller;

import com.example.appointmentservice.dto.AppointmentRequest;
import com.example.appointmentservice.dto.AddAppointmentRequest;
import com.example.appointmentservice.service.AppointmentService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/check-availability")
    public ResponseEntity<Object> checkAvailability(
            @Valid @RequestBody AppointmentRequest request) {
        return appointmentService.checkAvailability(request);
    }

    @PostMapping
    public ResponseEntity<Object> addAppointment(
            @Valid @RequestBody AddAppointmentRequest request) {
        return appointmentService.addAppointment(request);
    }

}
