package com.hospital.management.service;

import com.hospital.management.domain.entity.Bill;
import com.hospital.management.domain.entity.Patient;
import com.hospital.management.domain.entity.TenantBranding;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.BillRepository;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.repository.TenantBrandingRepository;
import com.hospital.management.util.SimplePdfBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoicePdfService {

    private final BillRepository billRepository;
    private final PatientRepository patientRepository;
    private final TenantBrandingRepository tenantBrandingRepository;

    public byte[] generateInvoice(UUID billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found: " + billId));
        Patient patient = patientRepository.findById(bill.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found for bill: " + billId));
        TenantBranding branding = tenantBrandingRepository.findAll().stream().findFirst().orElse(null);

        List<String> lines = new ArrayList<>();
        lines.add("HOSPITAL INVOICE");
        lines.add("Hospital: " + (branding != null ? branding.getHospitalName() : "Hospital"));
        lines.add("Logo URL: " + (branding != null ? branding.getLogoUrl() : "N/A"));
        lines.add("Address: " + patient.getAddress());
        lines.add("GST: " + (bill.getGstNumber() == null ? "N/A" : bill.getGstNumber()));
        lines.add(" ");
        lines.add("Invoice ID: " + bill.getId());
        lines.add("Patient: " + patient.getFirstName() + " " + patient.getLastName());
        lines.add("Amount: " + bill.getTotalAmount());
        lines.add("Paid: " + bill.getPaidAmount());
        lines.add("Status: " + bill.getStatus());
        lines.add("Description: " + (bill.getDescription() == null ? "N/A" : bill.getDescription()));
        return SimplePdfBuilder.fromLines(lines);
    }
}
