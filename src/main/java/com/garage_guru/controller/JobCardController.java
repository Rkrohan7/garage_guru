package com.garage_guru.controller;

import com.garage_guru.dto.request.AddItemsToJobCardRequest;
import com.garage_guru.dto.request.CompleteJobCardRequest;
import com.garage_guru.dto.request.JobCardItemRequest;
import com.garage_guru.dto.request.JobCardRequest;
import com.garage_guru.dto.response.InvoiceResponse;
import com.garage_guru.dto.response.JobCardCompletionResponse;
import com.garage_guru.dto.response.JobCardResponse;
import com.garage_guru.dto.response.MessageResponse;
import com.garage_guru.service.InvoiceService;
import com.garage_guru.service.JobCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-cards")
@RequiredArgsConstructor
public class JobCardController {

    private final JobCardService jobCardService;
    private final InvoiceService invoiceService;

    // ==================== JOB CARD CRUD ====================

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobCardResponse> createJobCard(@Valid @RequestBody JobCardRequest request) {
        JobCardResponse response = jobCardService.createJobCard(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobCardResponse> getJobCardById(@PathVariable Long id) {
        JobCardResponse response = jobCardService.getJobCardById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<JobCardResponse>> getAllJobCards() {
        List<JobCardResponse> response = jobCardService.getAllJobCards();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}")
    public ResponseEntity<List<JobCardResponse>> getJobCardsByGarageId(@PathVariable Long garageId) {
        List<JobCardResponse> response = jobCardService.getJobCardsByGarageId(garageId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobCardResponse> updateJobCard(@PathVariable Long id, @Valid @RequestBody JobCardRequest request) {
        JobCardResponse response = jobCardService.updateJobCard(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteJobCard(@PathVariable Long id) {
        jobCardService.deleteJobCard(id);
        return ResponseEntity.ok(MessageResponse.success("Job card deleted successfully"));
    }

    // ==================== JOB CARD STATUS ====================

    @GetMapping("/garage/{garageId}/flagged")
    public ResponseEntity<List<JobCardResponse>> getFlaggedJobCards(@PathVariable Long garageId) {
        List<JobCardResponse> response = jobCardService.getFlaggedJobCards(garageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobCardResponse>> searchByVehicleNumber(@RequestParam String vehicleNumber) {
        List<JobCardResponse> response = jobCardService.searchByVehicleNumber(vehicleNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/completed")
    public ResponseEntity<List<JobCardResponse>> getCompletedJobCards(@PathVariable Long garageId) {
        List<JobCardResponse> response = jobCardService.getCompletedJobCards(garageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/pending")
    public ResponseEntity<List<JobCardResponse>> getPendingJobCards(@PathVariable Long garageId) {
        List<JobCardResponse> response = jobCardService.getPendingJobCards(garageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/in-progress")
    public ResponseEntity<List<JobCardResponse>> getInProgressJobCards(@PathVariable Long garageId) {
        List<JobCardResponse> response = jobCardService.getInProgressJobCards(garageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/status")
    public ResponseEntity<List<JobCardResponse>> getJobCardsByStatus(
            @PathVariable Long garageId,
            @RequestParam String status) {
        List<JobCardResponse> response = jobCardService.getJobCardsByStatus(garageId, status);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobCardResponse> updateJobCardStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        JobCardResponse response = jobCardService.updateJobCardStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/start")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobCardResponse> startJobCard(@PathVariable Long id) {
        JobCardResponse response = jobCardService.startJobCard(id);
        return ResponseEntity.ok(response);
    }

    // ==================== JOB CARD ITEMS ====================

    @PostMapping("/{id}/items")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobCardResponse> addItemToJobCard(
            @PathVariable Long id,
            @Valid @RequestBody JobCardItemRequest request) {
        JobCardResponse response = jobCardService.addItemToJobCard(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/items/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobCardResponse> addItemsToJobCard(
            @PathVariable Long id,
            @Valid @RequestBody AddItemsToJobCardRequest request) {
        JobCardResponse response = jobCardService.addItemsToJobCard(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/items/{itemId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobCardResponse> updateJobCardItem(
            @PathVariable Long id,
            @PathVariable Long itemId,
            @Valid @RequestBody JobCardItemRequest request) {
        JobCardResponse response = jobCardService.updateJobCardItem(id, itemId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/items/{itemId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobCardResponse> removeItemFromJobCard(
            @PathVariable Long id,
            @PathVariable Long itemId) {
        JobCardResponse response = jobCardService.removeItemFromJobCard(id, itemId);
        return ResponseEntity.ok(response);
    }

    // ==================== COMPLETE JOB & GENERATE INVOICE ====================

    @PostMapping("/{id}/complete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobCardCompletionResponse> completeJobCard(
            @PathVariable Long id,
            @Valid @RequestBody CompleteJobCardRequest request) {

        // First complete the job card
        JobCardCompletionResponse completionResponse = jobCardService.completeJobCard(id, request);

        // If invoice generation is requested, create invoice with all items
        if (Boolean.TRUE.equals(request.generateInvoice())) {
            InvoiceResponse invoiceResponse = invoiceService.createInvoiceFromCompletedJobCard(
                    id,
                    request.invoiceDiscountAmount(),
                    request.invoiceTaxPercentage(),
                    request.invoiceNotes(),
                    request.termsAndConditions(),
                    request.dueDays()
            );
            return new ResponseEntity<>(
                    JobCardCompletionResponse.withInvoice(completionResponse.jobCard(), invoiceResponse),
                    HttpStatus.OK
            );
        }

        return ResponseEntity.ok(completionResponse);
    }
}