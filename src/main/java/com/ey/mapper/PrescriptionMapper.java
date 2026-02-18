package com.ey.mapper;

import org.springframework.stereotype.Component;

import com.ey.dto.request.PrescriptionRequest;
import com.ey.dto.response.PrescriptionResponse;
import com.ey.model.Appointment;
import com.ey.model.Prescription;

@Component
public class PrescriptionMapper {

    public Prescription toEntity(PrescriptionRequest req, Appointment appointment) {
        Prescription p = new Prescription();
        p.setAppointment(appointment);
        p.setDiagnosis(req.getDiagnosis());
        p.setMedicines(req.getMedicines());
        p.setNotes(req.getNotes());
        return p;
    }

    public PrescriptionResponse toResponse(Prescription p) {
        PrescriptionResponse r = new PrescriptionResponse();
        r.setPrescriptionId(p.getPrescriptionId());
        r.setAppointmentId(p.getAppointment().getAppointmentId());
        r.setDiagnosis(p.getDiagnosis());
        r.setMedicines(p.getMedicines());
        r.setNotes(p.getNotes());
        r.setStatus(p.getStatus());
        r.setCreatedAt(p.getCreatedAt());
        return r;
    }
}