package com.ey.dto.request;

import java.time.LocalDateTime;

import com.ey.enums.AppointmentStatus;
import com.ey.enums.AppointmentType;

public class AppointmentSearchRequest {

    private LocalDateTime start;
    private LocalDateTime end;
    private Long doctorId;
    private Long patientId;
    private AppointmentStatus status;
    private AppointmentType type;

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public AppointmentType getType() {
        return type;
    }

    public void setType(AppointmentType type) {
        this.type = type;
    }
}