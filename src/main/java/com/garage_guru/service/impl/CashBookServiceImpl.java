package com.garage_guru.service.impl;

import com.garage_guru.dto.request.CashBookRequest;
import com.garage_guru.dto.response.CashBookEntryResponse;
import com.garage_guru.dto.response.CashBookSummaryResponse;
import com.garage_guru.entity.CashBook;
import com.garage_guru.entity.Garage;
import com.garage_guru.exception.ResourceNotFoundException;
import com.garage_guru.repository.CashBookRepository;
import com.garage_guru.repository.GarageRepository;
import com.garage_guru.service.CashBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CashBookServiceImpl implements CashBookService {

    private final CashBookRepository cashBookRepository;
    private final GarageRepository garageRepository;

    @Override
    @Transactional
    public CashBookEntryResponse createEntry(CashBookRequest request) {
        Garage garage = garageRepository.findById(request.garageId())
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + request.garageId()));

        CashBook entry = CashBook.builder()
                .garage(garage)
                .transactionType(request.transactionType().toUpperCase())
                .category(request.category().toUpperCase())
                .amount(request.amount())
                .description(request.description())
                .paymentMethod(request.paymentMethod())
                .referenceNumber(request.referenceNumber())
                .partyName(request.partyName())
                .partyPhone(request.partyPhone())
                .invoiceId(request.invoiceId())
                .paymentId(request.paymentId())
                .expenseId(request.expenseId())
                .jobCardId(request.jobCardId())
                .transactionDate(request.transactionDate() != null ? request.transactionDate() : LocalDate.now())
                .notes(request.notes())
                .build();

        // Calculate running balance
        BigDecimal currentBalance = cashBookRepository.getNetBalance(request.garageId());
        if (currentBalance == null) currentBalance = BigDecimal.ZERO;

        if ("CREDIT".equals(entry.getTransactionType())) {
            entry.setRunningBalance(currentBalance.add(entry.getAmount()));
        } else {
            entry.setRunningBalance(currentBalance.subtract(entry.getAmount()));
        }

        entry = cashBookRepository.save(entry);
        return mapToResponse(entry);
    }

    @Override
    public CashBookEntryResponse getEntryById(Long id) {
        CashBook entry = cashBookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cash book entry not found with id: " + id));
        return mapToResponse(entry);
    }

    @Override
    public List<CashBookEntryResponse> getAllEntries() {
        return cashBookRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<CashBookEntryResponse> getEntriesByGarageId(Long garageId) {
        return cashBookRepository.findByGarageIdOrderByTransactionDateDescCreatedAtDesc(garageId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public CashBookEntryResponse updateEntry(Long id, CashBookRequest request) {
        CashBook entry = cashBookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cash book entry not found with id: " + id));

        Garage garage = garageRepository.findById(request.garageId())
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + request.garageId()));

        entry.setGarage(garage);
        entry.setTransactionType(request.transactionType().toUpperCase());
        entry.setCategory(request.category().toUpperCase());
        entry.setAmount(request.amount());
        entry.setDescription(request.description());
        entry.setPaymentMethod(request.paymentMethod());
        entry.setReferenceNumber(request.referenceNumber());
        entry.setPartyName(request.partyName());
        entry.setPartyPhone(request.partyPhone());
        entry.setInvoiceId(request.invoiceId());
        entry.setPaymentId(request.paymentId());
        entry.setExpenseId(request.expenseId());
        entry.setJobCardId(request.jobCardId());
        entry.setTransactionDate(request.transactionDate() != null ? request.transactionDate() : entry.getTransactionDate());
        entry.setNotes(request.notes());

        entry = cashBookRepository.save(entry);
        return mapToResponse(entry);
    }

    @Override
    @Transactional
    public void deleteEntry(Long id) {
        if (!cashBookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cash book entry not found with id: " + id);
        }
        cashBookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CashBookEntryResponse addCreditEntry(CashBookRequest request) {
        CashBookRequest creditRequest = new CashBookRequest(
                request.garageId(),
                "CREDIT",
                request.category(),
                request.amount(),
                request.description(),
                request.paymentMethod(),
                request.referenceNumber(),
                request.partyName(),
                request.partyPhone(),
                request.invoiceId(),
                request.paymentId(),
                request.expenseId(),
                request.jobCardId(),
                request.transactionDate(),
                request.notes()
        );
        return createEntry(creditRequest);
    }

    @Override
    public List<CashBookEntryResponse> getCreditEntries(Long garageId) {
        return cashBookRepository.findByGarageIdAndTransactionTypeOrderByTransactionDateDescCreatedAtDesc(garageId, "CREDIT")
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public CashBookEntryResponse addDebitEntry(CashBookRequest request) {
        CashBookRequest debitRequest = new CashBookRequest(
                request.garageId(),
                "DEBIT",
                request.category(),
                request.amount(),
                request.description(),
                request.paymentMethod(),
                request.referenceNumber(),
                request.partyName(),
                request.partyPhone(),
                request.invoiceId(),
                request.paymentId(),
                request.expenseId(),
                request.jobCardId(),
                request.transactionDate(),
                request.notes()
        );
        return createEntry(debitRequest);
    }

    @Override
    public List<CashBookEntryResponse> getDebitEntries(Long garageId) {
        return cashBookRepository.findByGarageIdAndTransactionTypeOrderByTransactionDateDescCreatedAtDesc(garageId, "DEBIT")
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<CashBookEntryResponse> getEntriesByCategory(Long garageId, String category) {
        return cashBookRepository.findByGarageIdAndCategoryOrderByTransactionDateDescCreatedAtDesc(garageId, category.toUpperCase())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<CashBookEntryResponse> getEntriesByDate(Long garageId, LocalDate date) {
        return cashBookRepository.findByGarageIdAndTransactionDateOrderByCreatedAtDesc(garageId, date)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<CashBookEntryResponse> getEntriesByDateRange(Long garageId, LocalDate startDate, LocalDate endDate) {
        return cashBookRepository.findByGarageIdAndTransactionDateBetweenOrderByTransactionDateDescCreatedAtDesc(
                garageId, startDate, endDate)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<CashBookEntryResponse> getTodayEntries(Long garageId) {
        return getEntriesByDate(garageId, LocalDate.now());
    }

    @Override
    public List<CashBookEntryResponse> searchByPartyName(Long garageId, String partyName) {
        return cashBookRepository.findByGarageIdAndPartyNameContainingIgnoreCaseOrderByTransactionDateDesc(garageId, partyName)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<CashBookEntryResponse> searchByDescription(Long garageId, String description) {
        return cashBookRepository.findByGarageIdAndDescriptionContainingIgnoreCaseOrderByTransactionDateDesc(garageId, description)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<CashBookEntryResponse> getEntriesByInvoice(Long invoiceId) {
        return cashBookRepository.findByInvoiceId(invoiceId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<CashBookEntryResponse> getEntriesByPayment(Long paymentId) {
        return cashBookRepository.findByPaymentId(paymentId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<CashBookEntryResponse> getEntriesByExpense(Long expenseId) {
        return cashBookRepository.findByExpenseId(expenseId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<CashBookEntryResponse> getEntriesByJobCard(Long jobCardId) {
        return cashBookRepository.findByJobCardId(jobCardId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public BigDecimal getTotalCredit(Long garageId) {
        BigDecimal total = cashBookRepository.getTotalCredit(garageId);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getTotalDebit(Long garageId) {
        BigDecimal total = cashBookRepository.getTotalDebit(garageId);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getNetBalance(Long garageId) {
        BigDecimal net = cashBookRepository.getNetBalance(garageId);
        return net != null ? net : BigDecimal.ZERO;
    }

    @Override
    public CashBookSummaryResponse getSummary(Long garageId) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + garageId));

        BigDecimal totalCredit = getTotalCredit(garageId);
        BigDecimal totalDebit = getTotalDebit(garageId);
        BigDecimal netBalance = getNetBalance(garageId);
        Long creditCount = cashBookRepository.countCreditTransactions(garageId);
        Long debitCount = cashBookRepository.countDebitTransactions(garageId);

        return CashBookSummaryResponse.of(
                garageId,
                garage.getName(),
                totalCredit,
                totalDebit,
                netBalance,
                creditCount,
                debitCount
        );
    }

    @Override
    public CashBookSummaryResponse getSummaryByDateRange(Long garageId, LocalDate startDate, LocalDate endDate) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + garageId));

        BigDecimal totalCredit = cashBookRepository.getTotalCreditByDateRange(garageId, startDate, endDate);
        BigDecimal totalDebit = cashBookRepository.getTotalDebitByDateRange(garageId, startDate, endDate);
        BigDecimal netBalance = cashBookRepository.getNetBalanceByDateRange(garageId, startDate, endDate);

        if (totalCredit == null) totalCredit = BigDecimal.ZERO;
        if (totalDebit == null) totalDebit = BigDecimal.ZERO;
        if (netBalance == null) netBalance = BigDecimal.ZERO;

        return CashBookSummaryResponse.ofDateRange(
                garageId,
                garage.getName(),
                totalCredit,
                totalDebit,
                netBalance,
                startDate,
                endDate
        );
    }

    @Override
    public CashBookSummaryResponse getTodaySummary(Long garageId) {
        LocalDate today = LocalDate.now();
        return getSummaryByDateRange(garageId, today, today);
    }

    @Override
    public CashBookSummaryResponse getMonthlySummary(Long garageId, int year, int month) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + garageId));

        BigDecimal totalCredit = cashBookRepository.getMonthlyCreditTotal(garageId, year, month);
        BigDecimal totalDebit = cashBookRepository.getMonthlyDebitTotal(garageId, year, month);

        if (totalCredit == null) totalCredit = BigDecimal.ZERO;
        if (totalDebit == null) totalDebit = BigDecimal.ZERO;

        BigDecimal netBalance = totalCredit.subtract(totalDebit);

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        return CashBookSummaryResponse.ofDateRange(
                garageId,
                garage.getName(),
                totalCredit,
                totalDebit,
                netBalance,
                startDate,
                endDate
        );
    }

    @Override
    @Transactional
    public CashBookEntryResponse createEntryFromPayment(Long garageId, Long paymentId, BigDecimal amount,
            String description, String partyName, String paymentMethod, Long jobCardId, Long invoiceId) {

        CashBookRequest request = new CashBookRequest(
                garageId,
                "CREDIT",
                "INVOICE_PAYMENT",
                amount,
                description,
                paymentMethod,
                null,
                partyName,
                null,
                invoiceId,
                paymentId,
                null,
                jobCardId,
                LocalDate.now(),
                null
        );

        return createEntry(request);
    }

    @Override
    @Transactional
    public CashBookEntryResponse createEntryFromExpense(Long garageId, Long expenseId, BigDecimal amount,
            String description, String category, String vendorName, String paymentMethod) {

        CashBookRequest request = new CashBookRequest(
                garageId,
                "DEBIT",
                category,
                amount,
                description,
                paymentMethod,
                null,
                vendorName,
                null,
                null,
                null,
                expenseId,
                null,
                LocalDate.now(),
                null
        );

        return createEntry(request);
    }

    @Override
    @Transactional
    public CashBookEntryResponse createEntryFromInvoicePayment(Long garageId, Long invoiceId, BigDecimal amount,
            String customerName, String paymentMethod) {

        CashBookRequest request = new CashBookRequest(
                garageId,
                "CREDIT",
                "INVOICE_PAYMENT",
                amount,
                "Payment received for Invoice #" + invoiceId,
                paymentMethod,
                "INV-" + invoiceId,
                customerName,
                null,
                invoiceId,
                null,
                null,
                null,
                LocalDate.now(),
                null
        );

        return createEntry(request);
    }

    private CashBookEntryResponse mapToResponse(CashBook entry) {
        return new CashBookEntryResponse(
                entry.getId(),
                entry.getGarage().getId(),
                entry.getGarage().getName(),
                entry.getTransactionType(),
                entry.getCategory(),
                entry.getAmount(),
                entry.getDescription(),
                entry.getPaymentMethod(),
                entry.getReferenceNumber(),
                entry.getPartyName(),
                entry.getPartyPhone(),
                entry.getInvoiceId(),
                entry.getPaymentId(),
                entry.getExpenseId(),
                entry.getJobCardId(),
                entry.getTransactionDate(),
                entry.getRunningBalance(),
                entry.getNotes(),
                entry.getCreatedAt(),
                entry.getUpdatedAt()
        );
    }
}
