package com.ey.dto.request;

import com.ey.enums.PrescriptionStatus;

public class PrescriptionUpdateRequest {
    private String diagnosis;
    private String medicines;
    private String notes;
    private PrescriptionStatus status;

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
}