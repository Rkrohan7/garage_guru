package com.garage_guru.service.impl;

import com.garage_guru.dto.request.ExpenseRequest;
import com.garage_guru.dto.response.CashbookResponse;
import com.garage_guru.dto.response.ExpenseResponse;
import com.garage_guru.dto.response.ExpenseSummaryResponse;
import com.garage_guru.entity.Expense;
import com.garage_guru.entity.Garage;
import com.garage_guru.exception.ResourceNotFoundException;
import com.garage_guru.repository.ExpenseRepository;
import com.garage_guru.repository.GarageRepository;
import com.garage_guru.repository.PaymentRepository;
import com.garage_guru.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final GarageRepository garageRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public ExpenseResponse createExpense(ExpenseRequest request) {
        Garage garage = garageRepository.findById(request.garageId())
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + request.garageId()));

        Expense expense = Expense.builder()
                .title(request.title())
                .description(request.description())
                .amount(request.amount())
                .category(request.category().toUpperCase())
                .paymentMethod(request.paymentMethod() != null ? request.paymentMethod().toUpperCase() : null)
                .vendorName(request.vendorName())
                .receiptNumber(request.receiptNumber())
                .expenseDate(request.expenseDate() != null ? request.expenseDate() : LocalDate.now())
                .garage(garage)
                .build();

        expense = expenseRepository.save(expense);
        return mapToResponse(expense);
    }

    @Override
    public ExpenseResponse getExpenseById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
        return mapToResponse(expense);
    }

    @Override
    public List<ExpenseResponse> getAllExpenses() {
        return expenseRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ExpenseResponse> getExpensesByGarageId(Long garageId) {
        return expenseRepository.findByGarageIdOrderByExpenseDateDesc(garageId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public ExpenseResponse updateExpense(Long id, ExpenseRequest request) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));

        Garage garage = garageRepository.findById(request.garageId())
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + request.garageId()));

        expense.setTitle(request.title());
        expense.setDescription(request.description());
        expense.setAmount(request.amount());
        expense.setCategory(request.category().toUpperCase());
        expense.setPaymentMethod(request.paymentMethod() != null ? request.paymentMethod().toUpperCase() : null);
        expense.setVendorName(request.vendorName());
        expense.setReceiptNumber(request.receiptNumber());
        expense.setExpenseDate(request.expenseDate() != null ? request.expenseDate() : expense.getExpenseDate());
        expense.setGarage(garage);

        expense = expenseRepository.save(expense);
        return mapToResponse(expense);
    }

    @Override
    @Transactional
    public void deleteExpense(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Expense not found with id: " + id);
        }
        expenseRepository.deleteById(id);
    }

    @Override
    public List<ExpenseResponse> getExpensesByDateRange(Long garageId, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findByGarageIdAndExpenseDateBetween(garageId, startDate, endDate).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ExpenseResponse> getExpensesByDate(Long garageId, LocalDate date) {
        return expenseRepository.findByGarageIdAndExpenseDate(garageId, date).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ExpenseResponse> getExpensesByCategory(Long garageId, String category) {
        return expenseRepository.findByGarageIdAndCategory(garageId, category.toUpperCase()).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ExpenseResponse> getExpensesByPaymentMethod(Long garageId, String paymentMethod) {
        return expenseRepository.findByGarageIdAndPaymentMethod(garageId, paymentMethod.toUpperCase()).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ExpenseResponse> getTodayExpenses(Long garageId) {
        return expenseRepository.findByGarageIdAndExpenseDate(garageId, LocalDate.now()).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ExpenseSummaryResponse getExpenseSummary(Long garageId) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + garageId));

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(7);
        LocalDate monthStart = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate yearStart = today.with(TemporalAdjusters.firstDayOfYear());

        BigDecimal totalExpenses = expenseRepository.getTotalExpensesByGarageId(garageId);
        BigDecimal todayExpenses = expenseRepository.getTodayExpensesByGarageId(garageId, today);
        BigDecimal weekExpenses = expenseRepository.getTotalExpensesByGarageIdAndDateRange(garageId, weekStart, today);
        BigDecimal monthExpenses = expenseRepository.getTotalExpensesByGarageIdAndDateRange(garageId, monthStart, today);
        BigDecimal yearExpenses = expenseRepository.getYearlyExpensesByGarageId(garageId, today.getYear());

        Long totalTransactions = expenseRepository.countByGarageId(garageId);
        Long todayTransactions = expenseRepository.countByGarageIdAndDateRange(garageId, today, today);

        // Category-wise expenses
        BigDecimal rentExpenses = expenseRepository.getTotalExpensesByGarageIdAndCategory(garageId, "RENT");
        BigDecimal salaryExpenses = expenseRepository.getTotalExpensesByGarageIdAndCategory(garageId, "SALARY");
        BigDecimal utilitiesExpenses = expenseRepository.getTotalExpensesByGarageIdAndCategory(garageId, "UTILITIES");
        BigDecimal maintenanceExpenses = expenseRepository.getTotalExpensesByGarageIdAndCategory(garageId, "MAINTENANCE");
        BigDecimal inventoryExpenses = expenseRepository.getTotalExpensesByGarageIdAndCategory(garageId, "INVENTORY");
        BigDecimal fuelExpenses = expenseRepository.getTotalExpensesByGarageIdAndCategory(garageId, "FUEL");
        BigDecimal toolsExpenses = expenseRepository.getTotalExpensesByGarageIdAndCategory(garageId, "TOOLS");
        BigDecimal marketingExpenses = expenseRepository.getTotalExpensesByGarageIdAndCategory(garageId, "MARKETING");
        BigDecimal insuranceExpenses = expenseRepository.getTotalExpensesByGarageIdAndCategory(garageId, "INSURANCE");
        BigDecimal taxExpenses = expenseRepository.getTotalExpensesByGarageIdAndCategory(garageId, "TAX");
        BigDecimal otherExpenses = expenseRepository.getTotalExpensesByGarageIdAndCategory(garageId, "OTHER");

        // Payment method-wise expenses
        BigDecimal cashExpenses = expenseRepository.getTotalExpensesByGarageIdAndPaymentMethod(garageId, "CASH");
        BigDecimal cardExpenses = expenseRepository.getTotalExpensesByGarageIdAndPaymentMethod(garageId, "CARD");
        BigDecimal upiExpenses = expenseRepository.getTotalExpensesByGarageIdAndPaymentMethod(garageId, "UPI");
        BigDecimal bankTransferExpenses = expenseRepository.getTotalExpensesByGarageIdAndPaymentMethod(garageId, "BANK_TRANSFER");

        return ExpenseSummaryResponse.of(
                garageId, garage.getName(),
                totalExpenses, todayExpenses, weekExpenses, monthExpenses, yearExpenses,
                totalTransactions, todayTransactions,
                rentExpenses, salaryExpenses, utilitiesExpenses, maintenanceExpenses,
                inventoryExpenses, fuelExpenses, toolsExpenses, marketingExpenses,
                insuranceExpenses, taxExpenses, otherExpenses,
                cashExpenses, cardExpenses, upiExpenses, bankTransferExpenses,
                null, null
        );
    }

    @Override
    public ExpenseSummaryResponse getExpenseSummaryByDateRange(Long garageId, LocalDate startDate, LocalDate endDate) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + garageId));

        BigDecimal totalExpenses = expenseRepository.getTotalExpensesByGarageIdAndDateRange(garageId, startDate, endDate);
        Long totalTransactions = expenseRepository.countByGarageIdAndDateRange(garageId, startDate, endDate);

        LocalDate today = LocalDate.now();
        BigDecimal todayExpenses = BigDecimal.ZERO;
        Long todayTransactions = 0L;

        if (!today.isBefore(startDate) && !today.isAfter(endDate)) {
            todayExpenses = expenseRepository.getTodayExpensesByGarageId(garageId, today);
            todayTransactions = expenseRepository.countByGarageIdAndDateRange(garageId, today, today);
        }

        // Category-wise expenses
        BigDecimal rentExpenses = expenseRepository.getTotalExpensesByGarageIdAndCategory(garageId, "RENT");
        BigDecimal salaryExpenses = expenseRepository.getTotalExpensesByGarageIdAndCategory(garageId, "SALARY");
        BigDecimal utilitiesExpenses = expenseRepository.getTotalExpensesByGarageIdAndCategory(garageId, "UTILITIES");
        BigDecimal maintenanceExpenses = expenseRepository.getTotalExpensesByGarageIdAndCategory(garageId, "MAINTENANCE");
        BigDecimal inventoryExpenses = expenseRepository.getTotalExpensesByGarageIdAndCategory(garageId, "INVENTORY");
        BigDecimal fuelExpenses = expenseRepository.getTotalExpensesByGarageIdAndCategory(garageId, "FUEL");
        BigDecimal toolsExpenses = expenseRepository.getTotalExpensesByGarageIdAndCategory(garageId, "TOOLS");
        BigDecimal marketingExpenses = expenseRepository.getTotalExpensesByGarageIdAndCategory(garageId, "MARKETING");
        BigDecimal insuranceExpenses = expenseRepository.getTotalExpensesByGarageIdAndCategory(garageId, "INSURANCE");
        BigDecimal taxExpenses = expenseRepository.getTotalExpensesByGarageIdAndCategory(garageId, "TAX");
        BigDecimal otherExpenses = expenseRepository.getTotalExpensesByGarageIdAndCategory(garageId, "OTHER");

        // Payment method-wise expenses
        BigDecimal cashExpenses = expenseRepository.getTotalExpensesByGarageIdAndPaymentMethod(garageId, "CASH");
        BigDecimal cardExpenses = expenseRepository.getTotalExpensesByGarageIdAndPaymentMethod(garageId, "CARD");
        BigDecimal upiExpenses = expenseRepository.getTotalExpensesByGarageIdAndPaymentMethod(garageId, "UPI");
        BigDecimal bankTransferExpenses = expenseRepository.getTotalExpensesByGarageIdAndPaymentMethod(garageId, "BANK_TRANSFER");

        return ExpenseSummaryResponse.of(
                garageId, garage.getName(),
                totalExpenses, todayExpenses, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                totalTransactions, todayTransactions,
                rentExpenses, salaryExpenses, utilitiesExpenses, maintenanceExpenses,
                inventoryExpenses, fuelExpenses, toolsExpenses, marketingExpenses,
                insuranceExpenses, taxExpenses, otherExpenses,
                cashExpenses, cardExpenses, upiExpenses, bankTransferExpenses,
                startDate, endDate
        );
    }

    @Override
    public CashbookResponse getCashbook(Long garageId) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + garageId));

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(7);
        LocalDate monthStart = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate yearStart = today.with(TemporalAdjusters.firstDayOfYear());

        // Income from payments
        BigDecimal totalIncome = paymentRepository.getTotalRevenueByGarageId(garageId);
        BigDecimal todayIncome = paymentRepository.getTodayRevenueByGarageId(garageId, today);
        BigDecimal weekIncome = paymentRepository.getTotalRevenueByGarageIdAndDateRange(garageId, weekStart, today);
        BigDecimal monthIncome = paymentRepository.getTotalRevenueByGarageIdAndDateRange(garageId, monthStart, today);
        BigDecimal yearIncome = paymentRepository.getYearlyRevenueByGarageId(garageId, today.getYear());
        Long totalIncomeTransactions = paymentRepository.countByGarageId(garageId);

        // Expenses
        BigDecimal totalExpenses = expenseRepository.getTotalExpensesByGarageId(garageId);
        BigDecimal todayExpenses = expenseRepository.getTodayExpensesByGarageId(garageId, today);
        BigDecimal weekExpenses = expenseRepository.getTotalExpensesByGarageIdAndDateRange(garageId, weekStart, today);
        BigDecimal monthExpenses = expenseRepository.getTotalExpensesByGarageIdAndDateRange(garageId, monthStart, today);
        BigDecimal yearExpenses = expenseRepository.getYearlyExpensesByGarageId(garageId, today.getYear());
        Long totalExpenseTransactions = expenseRepository.countByGarageId(garageId);

        return CashbookResponse.of(
                garageId, garage.getName(),
                totalIncome, todayIncome, weekIncome, monthIncome, yearIncome, totalIncomeTransactions,
                totalExpenses, todayExpenses, weekExpenses, monthExpenses, yearExpenses, totalExpenseTransactions,
                null, null
        );
    }

    @Override
    public CashbookResponse getCashbookByDateRange(Long garageId, LocalDate startDate, LocalDate endDate) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + garageId));

        LocalDate today = LocalDate.now();

        // Income from payments
        BigDecimal totalIncome = paymentRepository.getTotalRevenueByGarageIdAndDateRange(garageId, startDate, endDate);
        BigDecimal todayIncome = BigDecimal.ZERO;
        if (!today.isBefore(startDate) && !today.isAfter(endDate)) {
            todayIncome = paymentRepository.getTodayRevenueByGarageId(garageId, today);
        }
        Long totalIncomeTransactions = paymentRepository.countByGarageIdAndDateRange(garageId, startDate, endDate);

        // Expenses
        BigDecimal totalExpenses = expenseRepository.getTotalExpensesByGarageIdAndDateRange(garageId, startDate, endDate);
        BigDecimal todayExpenses = BigDecimal.ZERO;
        if (!today.isBefore(startDate) && !today.isAfter(endDate)) {
            todayExpenses = expenseRepository.getTodayExpensesByGarageId(garageId, today);
        }
        Long totalExpenseTransactions = expenseRepository.countByGarageIdAndDateRange(garageId, startDate, endDate);

        return CashbookResponse.of(
                garageId, garage.getName(),
                totalIncome, todayIncome, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, totalIncomeTransactions,
                totalExpenses, todayExpenses, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, totalExpenseTransactions,
                startDate, endDate
        );
    }

    private ExpenseResponse mapToResponse(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getTitle(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getCategory(),
                expense.getPaymentMethod(),
                expense.getVendorName(),
                expense.getReceiptNumber(),
                expense.getExpenseDate(),
                expense.getCreatedAt(),
                expense.getGarage().getId(),
                expense.getGarage().getName()
        );
    }
}
