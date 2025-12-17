package com.garage_guru.controller;

import com.garage_guru.dto.request.PaymentRequest;
import com.garage_guru.dto.response.MessageResponse;
import com.garage_guru.dto.response.PaymentResponse;
import com.garage_guru.dto.response.RevenueResponse;
import com.garage_guru.service.PaymentService;
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
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // ==================== PAYMENT CRUD ====================

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id) {
        PaymentResponse response = paymentService.getPaymentById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        List<PaymentResponse> response = paymentService.getAllPayments();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByGarageId(@PathVariable Long garageId) {
        List<PaymentResponse> response = paymentService.getPaymentsByGarageId(garageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/job-card/{jobCardId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByJobCardId(@PathVariable Long jobCardId) {
        List<PaymentResponse> response = paymentService.getPaymentsByJobCardId(jobCardId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> updatePayment(@PathVariable Long id, @Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.updatePayment(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.ok(MessageResponse.success("Payment deleted successfully"));
    }

    // ==================== PAYMENT FILTERS ====================

    @GetMapping("/garage/{garageId}/today")
    public ResponseEntity<List<PaymentResponse>> getTodayPayments(@PathVariable Long garageId) {
        List<PaymentResponse> response = paymentService.getTodayPayments(garageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/date")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByDate(
            @PathVariable Long garageId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<PaymentResponse> response = paymentService.getPaymentsByDate(garageId, date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/date-range")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByDateRange(
            @PathVariable Long garageId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<PaymentResponse> response = paymentService.getPaymentsByDateRange(garageId, startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/type/{paymentType}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByPaymentType(
            @PathVariable Long garageId,
            @PathVariable String paymentType) {
        List<PaymentResponse> response = paymentService.getPaymentsByPaymentType(garageId, paymentType);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/method/{paymentMethod}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByPaymentMethod(
            @PathVariable Long garageId,
            @PathVariable String paymentMethod) {
        List<PaymentResponse> response = paymentService.getPaymentsByPaymentMethod(garageId, paymentMethod);
        return ResponseEntity.ok(response);
    }

    // ==================== REVENUE REPORTS ====================

    @GetMapping("/garage/{garageId}/revenue")
    public ResponseEntity<RevenueResponse> getRevenueReport(@PathVariable Long garageId) {
        RevenueResponse response = paymentService.getRevenueReport(garageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/revenue/date-range")
    public ResponseEntity<RevenueResponse> getRevenueReportByDateRange(
            @PathVariable Long garageId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        RevenueResponse response = paymentService.getRevenueReportByDateRange(garageId, startDate, endDate);
        return ResponseEntity.ok(response);
    }
}