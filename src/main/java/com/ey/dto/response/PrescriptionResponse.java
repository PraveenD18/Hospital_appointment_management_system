package com.ey.dto.response;

import java.time.LocalDateTime;

import com.ey.enums.PrescriptionStatus;

public class PrescriptionResponse {
    private Long prescriptionId;
    private Long appointmentId;
    private String diagnosis;
    private String medicines;
    private String notes;
    private PrescriptionStatus status;
    private LocalDateTime createdAt;

    public Long getPrescriptionId() {
        return prescriptionId;
    }
    public void setPrescriptionId(Long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }
    public Long getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }
    public String getDiagnosis() {
        return diagnosis;
    }
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
    public String getMedicines() {
        return medicines;
    }
    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public PrescriptionStatus getStatus() {
        return status;
    }
    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}