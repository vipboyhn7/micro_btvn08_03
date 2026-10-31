package com.example.appointmentservice.service;

import com.example.appointmentservice.dto.ApiResponseError;
import com.example.appointmentservice.dto.DoctorAvailabilityResponse;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
public class DoctorService {

    private final RestTemplate doctorServiceClient;

    public DoctorService(RestTemplate doctorServiceClient) {
        this.doctorServiceClient = doctorServiceClient;
    }

    @Retry(name = "patientRetry", fallbackMethod = "getDoctorFallback")
    public Object getDoctor(String doctorId, String date, String time) {
        DoctorAvailabilityResponse response = doctorServiceClient.getForObject(
                "/api/doctors/{doctorId}/availability?date={date}&time={time}",
                DoctorAvailabilityResponse.class,
                doctorId,
                date,
                time);
        if (response == null) {
            throw new IllegalStateException("Doctor-Service returned an empty response");
        }
        return response;
    }

    public ApiResponseError getDoctorFallback(String doctorId, String date, String time, Exception e) {
        return new ApiResponseError(
                "Hiện tại không thể kiểm tra thông tin bác sĩ, vui lòng thử lại sau vài giây",
                503,
                "Doctor Service Error",
                Instant.now());
    }

    public Object searchDoctors(String name) {
        return doctorServiceClient.getForObject(
                "/api/doctors/search?name={name}",
                Object.class,
                name);
    }
}
