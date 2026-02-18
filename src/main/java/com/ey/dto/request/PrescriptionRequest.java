package com.ey.dto.request;

public class PrescriptionRequest {
    private Long appointmentId;
    private String diagnosis;
    private String medicines;
    private String notes;

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
}