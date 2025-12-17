package com.garage_guru.service;

import com.garage_guru.dto.request.ExpenseRequest;
import com.garage_guru.dto.response.CashbookResponse;
import com.garage_guru.dto.response.ExpenseResponse;
import com.garage_guru.dto.response.ExpenseSummaryResponse;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {

    // Expense CRUD
    ExpenseResponse createExpense(ExpenseRequest request);
    ExpenseResponse getExpenseById(Long id);
    List<ExpenseResponse> getAllExpenses();
    List<ExpenseResponse> getExpensesByGarageId(Long garageId);
    ExpenseResponse updateExpense(Long id, ExpenseRequest request);
    void deleteExpense(Long id);

    // Expense filters
    List<ExpenseResponse> getExpensesByDateRange(Long garageId, LocalDate startDate, LocalDate endDate);
    List<ExpenseResponse> getExpensesByDate(Long garageId, LocalDate date);
    List<ExpenseResponse> getExpensesByCategory(Long garageId, String category);
    List<ExpenseResponse> getExpensesByPaymentMethod(Long garageId, String paymentMethod);
    List<ExpenseResponse> getTodayExpenses(Long garageId);

    // Expense reports
    ExpenseSummaryResponse getExpenseSummary(Long garageId);
    ExpenseSummaryResponse getExpenseSummaryByDateRange(Long garageId, LocalDate startDate, LocalDate endDate);

    // Cashbook (Income vs Expenses)
    CashbookResponse getCashbook(Long garageId);
    CashbookResponse getCashbookByDateRange(Long garageId, LocalDate startDate, LocalDate endDate);
}
