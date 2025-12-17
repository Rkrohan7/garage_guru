package com.garage_guru.service;

import com.garage_guru.dto.request.InvoicePaymentRequest;
import com.garage_guru.dto.request.InvoiceRequest;
import com.garage_guru.dto.response.InvoiceResponse;
import com.garage_guru.dto.response.InvoiceSummaryResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface InvoiceService {

    // Invoice CRUD
    InvoiceResponse createInvoice(InvoiceRequest request);
    InvoiceResponse getInvoiceById(Long id);
    InvoiceResponse getInvoiceByNumber(String invoiceNumber);
    List<InvoiceResponse> getAllInvoices();
    List<InvoiceResponse> getInvoicesByGarageId(Long garageId);
    InvoiceResponse updateInvoice(Long id, InvoiceRequest request);
    void deleteInvoice(Long id);

    // Invoice from Job Card
    InvoiceResponse createInvoiceFromJobCard(Long jobCardId);

    // Create invoice from completed job card with all items
    InvoiceResponse createInvoiceFromCompletedJobCard(Long jobCardId, BigDecimal invoiceDiscountAmount,
            BigDecimal invoiceTaxPercentage, String invoiceNotes, String termsAndConditions, Integer dueDays);

    // Invoice status
    InvoiceResponse updateInvoiceStatus(Long id, String status);
    InvoiceResponse markAsSent(Long id);
    InvoiceResponse markAsPaid(Long id);
    InvoiceResponse cancelInvoice(Long id);

    // Invoice payment
    InvoiceResponse addPayment(Long id, InvoicePaymentRequest request);

    // Invoice filters
    List<InvoiceResponse> getInvoicesByStatus(Long garageId, String status);
    List<InvoiceResponse> getInvoicesByPaymentStatus(Long garageId, String paymentStatus);
    List<InvoiceResponse> getInvoicesByDateRange(Long garageId, LocalDate startDate, LocalDate endDate);
    List<InvoiceResponse> getInvoicesByDate(Long garageId, LocalDate date);
    List<InvoiceResponse> getTodayInvoices(Long garageId);
    List<InvoiceResponse> getOverdueInvoices(Long garageId);

    // Search
    List<InvoiceResponse> searchByCustomerName(Long garageId, String customerName);
    List<InvoiceResponse> searchByVehicleNumber(Long garageId, String vehicleNumber);
    List<InvoiceResponse> getInvoicesByJobCard(Long jobCardId);

    // Invoice reports
    InvoiceSummaryResponse getInvoiceSummary(Long garageId);
    InvoiceSummaryResponse getInvoiceSummaryByDateRange(Long garageId, LocalDate startDate, LocalDate endDate);
}
