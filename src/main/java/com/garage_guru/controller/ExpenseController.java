package com.garage_guru.controller;

import com.garage_guru.dto.request.ExpenseRequest;
import com.garage_guru.dto.response.CashbookResponse;
import com.garage_guru.dto.response.ExpenseResponse;
import com.garage_guru.dto.response.ExpenseSummaryResponse;
import com.garage_guru.dto.response.MessageResponse;
import com.garage_guru.service.ExpenseService;
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
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    // ==================== EXPENSE CRUD ====================

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExpenseResponse> createExpense(@Valid @RequestBody ExpenseRequest request) {
        ExpenseResponse response = expenseService.createExpense(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable Long id) {
        ExpenseResponse response = expenseService.getExpenseById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {
        List<ExpenseResponse> response = expenseService.getAllExpenses();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByGarageId(@PathVariable Long garageId) {
        List<ExpenseResponse> response = expenseService.getExpensesByGarageId(garageId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExpenseResponse> updateExpense(@PathVariable Long id, @Valid @RequestBody ExpenseRequest request) {
        ExpenseResponse response = expenseService.updateExpense(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.ok(MessageResponse.success("Expense deleted successfully"));
    }

    // ==================== EXPENSE FILTERS ====================

    @GetMapping("/garage/{garageId}/today")
    public ResponseEntity<List<ExpenseResponse>> getTodayExpenses(@PathVariable Long garageId) {
        List<ExpenseResponse> response = expenseService.getTodayExpenses(garageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/date")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByDate(
            @PathVariable Long garageId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ExpenseResponse> response = expenseService.getExpensesByDate(garageId, date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/date-range")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByDateRange(
            @PathVariable Long garageId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<ExpenseResponse> response = expenseService.getExpensesByDateRange(garageId, startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/category/{category}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByCategory(
            @PathVariable Long garageId,
            @PathVariable String category) {
        List<ExpenseResponse> response = expenseService.getExpensesByCategory(garageId, category);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/method/{paymentMethod}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByPaymentMethod(
            @PathVariable Long garageId,
            @PathVariable String paymentMethod) {
        List<ExpenseResponse> response = expenseService.getExpensesByPaymentMethod(garageId, paymentMethod);
        return ResponseEntity.ok(response);
    }

    // ==================== EXPENSE SUMMARY ====================

    @GetMapping("/garage/{garageId}/summary")
    public ResponseEntity<ExpenseSummaryResponse> getExpenseSummary(@PathVariable Long garageId) {
        ExpenseSummaryResponse response = expenseService.getExpenseSummary(garageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/summary/date-range")
    public ResponseEntity<ExpenseSummaryResponse> getExpenseSummaryByDateRange(
            @PathVariable Long garageId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        ExpenseSummaryResponse response = expenseService.getExpenseSummaryByDateRange(garageId, startDate, endDate);
        return ResponseEntity.ok(response);
    }

    // ==================== CASHBOOK (INCOME VS EXPENSES) ====================

    @GetMapping("/garage/{garageId}/cashbook")
    public ResponseEntity<CashbookResponse> getCashbook(@PathVariable Long garageId) {
        CashbookResponse response = expenseService.getCashbook(garageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/cashbook/date-range")
    public ResponseEntity<CashbookResponse> getCashbookByDateRange(
            @PathVariable Long garageId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        CashbookResponse response = expenseService.getCashbookByDateRange(garageId, startDate, endDate);
        return ResponseEntity.ok(response);
    }
}
