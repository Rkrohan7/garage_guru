package com.garage_guru.service;

import com.garage_guru.dto.request.PaymentRequest;
import com.garage_guru.dto.response.PaymentResponse;
import com.garage_guru.dto.response.RevenueResponse;

import java.time.LocalDate;
import java.util.List;

public interface PaymentService {

    // Payment CRUD
    PaymentResponse createPayment(PaymentRequest request);
    PaymentResponse getPaymentById(Long id);
    List<PaymentResponse> getAllPayments();
    List<PaymentResponse> getPaymentsByGarageId(Long garageId);
    List<PaymentResponse> getPaymentsByJobCardId(Long jobCardId);
    PaymentResponse updatePayment(Long id, PaymentRequest request);
    void deletePayment(Long id);

    // Payment filters
    List<PaymentResponse> getPaymentsByDateRange(Long garageId, LocalDate startDate, LocalDate endDate);
    List<PaymentResponse> getPaymentsByDate(Long garageId, LocalDate date);
    List<PaymentResponse> getPaymentsByPaymentType(Long garageId, String paymentType);
    List<PaymentResponse> getPaymentsByPaymentMethod(Long garageId, String paymentMethod);
    List<PaymentResponse> getTodayPayments(Long garageId);

    // Revenue reports
    RevenueResponse getRevenueReport(Long garageId);
    RevenueResponse getRevenueReportByDateRange(Long garageId, LocalDate startDate, LocalDate endDate);
}
