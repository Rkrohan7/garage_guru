package com.garage_guru.controller;

import com.garage_guru.dto.request.InvoicePaymentRequest;
import com.garage_guru.dto.request.InvoiceRequest;
import com.garage_guru.dto.response.InvoiceResponse;
import com.garage_guru.dto.response.InvoiceSummaryResponse;
import com.garage_guru.dto.response.MessageResponse;
import com.garage_guru.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    // ==================== INVOICE CRUD ====================

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InvoiceResponse> createInvoice(@Valid @RequestBody InvoiceRequest request) {
        InvoiceResponse response = invoiceService.createInvoice(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable Long id) {
        InvoiceResponse response = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/number/{invoiceNumber}")
    public ResponseEntity<InvoiceResponse> getInvoiceByNumber(@PathVariable String invoiceNumber) {
        InvoiceResponse response = invoiceService.getInvoiceByNumber(invoiceNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        List<InvoiceResponse> response = invoiceService.getAllInvoices();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}")
    public ResponseEntity<List<InvoiceResponse>> getInvoicesByGarageId(@PathVariable Long garageId) {
        List<InvoiceResponse> response = invoiceService.getInvoicesByGarageId(garageId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InvoiceResponse> updateInvoice(@PathVariable Long id, @Valid @RequestBody InvoiceRequest request) {
        InvoiceResponse response = invoiceService.updateInvoice(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.ok(MessageResponse.success("Invoice deleted successfully"));
    }

    // ==================== INVOICE FROM JOB CARD ====================

    @PostMapping("/from-job-card/{jobCardId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InvoiceResponse> createInvoiceFromJobCard(@PathVariable Long jobCardId) {
        InvoiceResponse response = invoiceService.createInvoiceFromJobCard(jobCardId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/job-card/{jobCardId}")
    public ResponseEntity<List<InvoiceResponse>> getInvoicesByJobCard(@PathVariable Long jobCardId) {
        List<InvoiceResponse> response = invoiceService.getInvoicesByJobCard(jobCardId);
        return ResponseEntity.ok(response);
    }

    // ==================== INVOICE STATUS ====================

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InvoiceResponse> updateInvoiceStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        InvoiceResponse response = invoiceService.updateInvoiceStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/send")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InvoiceResponse> markAsSent(@PathVariable Long id) {
        InvoiceResponse response = invoiceService.markAsSent(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/mark-paid")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InvoiceResponse> markAsPaid(@PathVariable Long id) {
        InvoiceResponse response = invoiceService.markAsPaid(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InvoiceResponse> cancelInvoice(@PathVariable Long id) {
        InvoiceResponse response = invoiceService.cancelInvoice(id);
        return ResponseEntity.ok(response);
    }

    // ==================== INVOICE PAYMENT ====================

    @PostMapping("/{id}/payment")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InvoiceResponse> addPayment(
            @PathVariable Long id,
            @Valid @RequestBody InvoicePaymentRequest request) {
        InvoiceResponse response = invoiceService.addPayment(id, request);
        return ResponseEntity.ok(response);
    }

    // ==================== INVOICE FILTERS ====================

    @GetMapping("/garage/{garageId}/status/{status}")
    public ResponseEntity<List<InvoiceResponse>> getInvoicesByStatus(
            @PathVariable Long garageId,
            @PathVariable String status) {
        List<InvoiceResponse> response = invoiceService.getInvoicesByStatus(garageId, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/payment-status/{paymentStatus}")
    public ResponseEntity<List<InvoiceResponse>> getInvoicesByPaymentStatus(
            @PathVariable Long garageId,
            @PathVariable String paymentStatus) {
        List<InvoiceResponse> response = invoiceService.getInvoicesByPaymentStatus(garageId, paymentStatus);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/today")
    public ResponseEntity<List<InvoiceResponse>> getTodayInvoices(@PathVariable Long garageId) {
        List<InvoiceResponse> response = invoiceService.getTodayInvoices(garageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/date")
    public ResponseEntity<List<InvoiceResponse>> getInvoicesByDate(
            @PathVariable Long garageId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<InvoiceResponse> response = invoiceService.getInvoicesByDate(garageId, date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/date-range")
    public ResponseEntity<List<InvoiceResponse>> getInvoicesByDateRange(
            @PathVariable Long garageId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<InvoiceResponse> response = invoiceService.getInvoicesByDateRange(garageId, startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/overdue")
    public ResponseEntity<List<InvoiceResponse>> getOverdueInvoices(@PathVariable Long garageId) {
        List<InvoiceResponse> response = invoiceService.getOverdueInvoices(garageId);
        return ResponseEntity.ok(response);
    }

    // ==================== INVOICE SEARCH ====================

    @GetMapping("/garage/{garageId}/search/customer")
    public ResponseEntity<List<InvoiceResponse>> searchByCustomerName(
            @PathVariable Long garageId,
            @RequestParam String name) {
        List<InvoiceResponse> response = invoiceService.searchByCustomerName(garageId, name);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/search/vehicle")
    public ResponseEntity<List<InvoiceResponse>> searchByVehicleNumber(
            @PathVariable Long garageId,
            @RequestParam String vehicleNumber) {
        List<InvoiceResponse> response = invoiceService.searchByVehicleNumber(garageId, vehicleNumber);
        return ResponseEntity.ok(response);
    }

    // ==================== INVOICE SUMMARY ====================

    @GetMapping("/garage/{garageId}/summary")
    public ResponseEntity<InvoiceSummaryResponse> getInvoiceSummary(@PathVariable Long garageId) {
        InvoiceSummaryResponse response = invoiceService.getInvoiceSummary(garageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/summary/date-range")
    public ResponseEntity<InvoiceSummaryResponse> getInvoiceSummaryByDateRange(
            @PathVariable Long garageId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        InvoiceSummaryResponse response = invoiceService.getInvoiceSummaryByDateRange(garageId, startDate, endDate);
        return ResponseEntity.ok(response);
    }
}