package com.garage_guru.service;

import com.garage_guru.dto.request.CashBookRequest;
import com.garage_guru.dto.response.CashBookEntryResponse;
import com.garage_guru.dto.response.CashBookSummaryResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface CashBookService {

    // CRUD operations
    CashBookEntryResponse createEntry(CashBookRequest request);
    CashBookEntryResponse getEntryById(Long id);
    List<CashBookEntryResponse> getAllEntries();
    List<CashBookEntryResponse> getEntriesByGarageId(Long garageId);
    CashBookEntryResponse updateEntry(Long id, CashBookRequest request);
    void deleteEntry(Long id);

    // Credit (Income) entries
    CashBookEntryResponse addCreditEntry(CashBookRequest request);
    List<CashBookEntryResponse> getCreditEntries(Long garageId);

    // Debit (Expense) entries
    CashBookEntryResponse addDebitEntry(CashBookRequest request);
    List<CashBookEntryResponse> getDebitEntries(Long garageId);

    // Filter by category
    List<CashBookEntryResponse> getEntriesByCategory(Long garageId, String category);

    // Filter by date
    List<CashBookEntryResponse> getEntriesByDate(Long garageId, LocalDate date);
    List<CashBookEntryResponse> getEntriesByDateRange(Long garageId, LocalDate startDate, LocalDate endDate);
    List<CashBookEntryResponse> getTodayEntries(Long garageId);

    // Search
    List<CashBookEntryResponse> searchByPartyName(Long garageId, String partyName);
    List<CashBookEntryResponse> searchByDescription(Long garageId, String description);

    // Related entity queries
    List<CashBookEntryResponse> getEntriesByInvoice(Long invoiceId);
    List<CashBookEntryResponse> getEntriesByPayment(Long paymentId);
    List<CashBookEntryResponse> getEntriesByExpense(Long expenseId);
    List<CashBookEntryResponse> getEntriesByJobCard(Long jobCardId);

    // Balance and Summary
    BigDecimal getTotalCredit(Long garageId);
    BigDecimal getTotalDebit(Long garageId);
    BigDecimal getNetBalance(Long garageId);

    CashBookSummaryResponse getSummary(Long garageId);
    CashBookSummaryResponse getSummaryByDateRange(Long garageId, LocalDate startDate, LocalDate endDate);
    CashBookSummaryResponse getTodaySummary(Long garageId);
    CashBookSummaryResponse getMonthlySummary(Long garageId, int year, int month);

    // Auto-entry methods (called from other services)
    CashBookEntryResponse createEntryFromPayment(Long garageId, Long paymentId, BigDecimal amount,
            String description, String partyName, String paymentMethod, Long jobCardId, Long invoiceId);

    CashBookEntryResponse createEntryFromExpense(Long garageId, Long expenseId, BigDecimal amount,
            String description, String category, String vendorName, String paymentMethod);

    CashBookEntryResponse createEntryFromInvoicePayment(Long garageId, Long invoiceId, BigDecimal amount,
            String customerName, String paymentMethod);
}
