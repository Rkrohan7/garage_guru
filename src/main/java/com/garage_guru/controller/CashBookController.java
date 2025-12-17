package com.garage_guru.controller;

import com.garage_guru.dto.request.CashBookRequest;
import com.garage_guru.dto.response.CashBookEntryResponse;
import com.garage_guru.dto.response.CashBookSummaryResponse;
import com.garage_guru.dto.response.MessageResponse;
import com.garage_guru.service.CashBookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cashbook")
@RequiredArgsConstructor
public class CashBookController {

    private final CashBookService cashBookService;

    // ==================== CRUD Operations ====================

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CashBookEntryResponse> createEntry(@Valid @RequestBody CashBookRequest request) {
        CashBookEntryResponse response = cashBookService.createEntry(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CashBookEntryResponse> getEntryById(@PathVariable Long id) {
        CashBookEntryResponse response = cashBookService.getEntryById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CashBookEntryResponse>> getAllEntries() {
        List<CashBookEntryResponse> response = cashBookService.getAllEntries();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}")
    public ResponseEntity<List<CashBookEntryResponse>> getEntriesByGarageId(@PathVariable Long garageId) {
        List<CashBookEntryResponse> response = cashBookService.getEntriesByGarageId(garageId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CashBookEntryResponse> updateEntry(
            @PathVariable Long id,
            @Valid @RequestBody CashBookRequest request) {
        CashBookEntryResponse response = cashBookService.updateEntry(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteEntry(@PathVariable Long id) {
        cashBookService.deleteEntry(id);
        return ResponseEntity.ok(MessageResponse.success("Cash book entry deleted successfully"));
    }

    // ==================== Credit (Income) Operations ====================

    @PostMapping("/credit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CashBookEntryResponse> addCreditEntry(@Valid @RequestBody CashBookRequest request) {
        CashBookEntryResponse response = cashBookService.addCreditEntry(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/garage/{garageId}/credit")
    public ResponseEntity<List<CashBookEntryResponse>> getCreditEntries(@PathVariable Long garageId) {
        List<CashBookEntryResponse> response = cashBookService.getCreditEntries(garageId);
        return ResponseEntity.ok(response);
    }

    // ==================== Debit (Expense) Operations ====================

    @PostMapping("/debit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CashBookEntryResponse> addDebitEntry(@Valid @RequestBody CashBookRequest request) {
        CashBookEntryResponse response = cashBookService.addDebitEntry(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/garage/{garageId}/debit")
    public ResponseEntity<List<CashBookEntryResponse>> getDebitEntries(@PathVariable Long garageId) {
        List<CashBookEntryResponse> response = cashBookService.getDebitEntries(garageId);
        return ResponseEntity.ok(response);
    }

    // ==================== Filter Operations ====================

    @GetMapping("/garage/{garageId}/category/{category}")
    public ResponseEntity<List<CashBookEntryResponse>> getEntriesByCategory(
            @PathVariable Long garageId,
            @PathVariable String category) {
        List<CashBookEntryResponse> response = cashBookService.getEntriesByCategory(garageId, category);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/date")
    public ResponseEntity<List<CashBookEntryResponse>> getEntriesByDate(
            @PathVariable Long garageId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<CashBookEntryResponse> response = cashBookService.getEntriesByDate(garageId, date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/date-range")
    public ResponseEntity<List<CashBookEntryResponse>> getEntriesByDateRange(
            @PathVariable Long garageId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<CashBookEntryResponse> response = cashBookService.getEntriesByDateRange(garageId, startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/today")
    public ResponseEntity<List<CashBookEntryResponse>> getTodayEntries(@PathVariable Long garageId) {
        List<CashBookEntryResponse> response = cashBookService.getTodayEntries(garageId);
        return ResponseEntity.ok(response);
    }

    // ==================== Search Operations ====================

    @GetMapping("/garage/{garageId}/search/party")
    public ResponseEntity<List<CashBookEntryResponse>> searchByPartyName(
            @PathVariable Long garageId,
            @RequestParam String name) {
        List<CashBookEntryResponse> response = cashBookService.searchByPartyName(garageId, name);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/search/description")
    public ResponseEntity<List<CashBookEntryResponse>> searchByDescription(
            @PathVariable Long garageId,
            @RequestParam String description) {
        List<CashBookEntryResponse> response = cashBookService.searchByDescription(garageId, description);
        return ResponseEntity.ok(response);
    }

    // ==================== Related Entity Queries ====================

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<CashBookEntryResponse>> getEntriesByInvoice(@PathVariable Long invoiceId) {
        List<CashBookEntryResponse> response = cashBookService.getEntriesByInvoice(invoiceId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<List<CashBookEntryResponse>> getEntriesByPayment(@PathVariable Long paymentId) {
        List<CashBookEntryResponse> response = cashBookService.getEntriesByPayment(paymentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/expense/{expenseId}")
    public ResponseEntity<List<CashBookEntryResponse>> getEntriesByExpense(@PathVariable Long expenseId) {
        List<CashBookEntryResponse> response = cashBookService.getEntriesByExpense(expenseId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/job-card/{jobCardId}")
    public ResponseEntity<List<CashBookEntryResponse>> getEntriesByJobCard(@PathVariable Long jobCardId) {
        List<CashBookEntryResponse> response = cashBookService.getEntriesByJobCard(jobCardId);
        return ResponseEntity.ok(response);
    }

    // ==================== Balance & Totals ====================

    @GetMapping("/garage/{garageId}/balance")
    public ResponseEntity<Map<String, BigDecimal>> getBalance(@PathVariable Long garageId) {
        BigDecimal totalCredit = cashBookService.getTotalCredit(garageId);
        BigDecimal totalDebit = cashBookService.getTotalDebit(garageId);
        BigDecimal netBalance = cashBookService.getNetBalance(garageId);

        Map<String, BigDecimal> balances = Map.of(
                "totalCredit", totalCredit,
                "totalDebit", totalDebit,
                "netBalance", netBalance
        );
        return ResponseEntity.ok(balances);
    }

    @GetMapping("/garage/{garageId}/total-credit")
    public ResponseEntity<Map<String, BigDecimal>> getTotalCredit(@PathVariable Long garageId) {
        BigDecimal total = cashBookService.getTotalCredit(garageId);
        return ResponseEntity.ok(Map.of("totalCredit", total));
    }

    @GetMapping("/garage/{garageId}/total-debit")
    public ResponseEntity<Map<String, BigDecimal>> getTotalDebit(@PathVariable Long garageId) {
        BigDecimal total = cashBookService.getTotalDebit(garageId);
        return ResponseEntity.ok(Map.of("totalDebit", total));
    }

    @GetMapping("/garage/{garageId}/net-balance")
    public ResponseEntity<Map<String, BigDecimal>> getNetBalance(@PathVariable Long garageId) {
        BigDecimal net = cashBookService.getNetBalance(garageId);
        return ResponseEntity.ok(Map.of("netBalance", net));
    }

    // ==================== Summary Reports ====================

    @GetMapping("/garage/{garageId}/summary")
    public ResponseEntity<CashBookSummaryResponse> getSummary(@PathVariable Long garageId) {
        CashBookSummaryResponse response = cashBookService.getSummary(garageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/summary/date-range")
    public ResponseEntity<CashBookSummaryResponse> getSummaryByDateRange(
            @PathVariable Long garageId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        CashBookSummaryResponse response = cashBookService.getSummaryByDateRange(garageId, startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/summary/today")
    public ResponseEntity<CashBookSummaryResponse> getTodaySummary(@PathVariable Long garageId) {
        CashBookSummaryResponse response = cashBookService.getTodaySummary(garageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/summary/monthly")
    public ResponseEntity<CashBookSummaryResponse> getMonthlySummary(
            @PathVariable Long garageId,
            @RequestParam int year,
            @RequestParam int month) {
        CashBookSummaryResponse response = cashBookService.getMonthlySummary(garageId, year, month);
        return ResponseEntity.ok(response);
    }
}
