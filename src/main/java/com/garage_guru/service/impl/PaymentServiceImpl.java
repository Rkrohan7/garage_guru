package com.garage_guru.service.impl;

import com.garage_guru.dto.request.PaymentRequest;
import com.garage_guru.dto.response.PaymentResponse;
import com.garage_guru.dto.response.RevenueResponse;
import com.garage_guru.entity.Garage;
import com.garage_guru.entity.JobCard;
import com.garage_guru.entity.Payment;
import com.garage_guru.exception.ResourceNotFoundException;
import com.garage_guru.repository.GarageRepository;
import com.garage_guru.repository.JobCardRepository;
import com.garage_guru.repository.PaymentRepository;
import com.garage_guru.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final GarageRepository garageRepository;
    private final JobCardRepository jobCardRepository;

    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        Garage garage = garageRepository.findById(request.garageId())
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + request.garageId()));

        Payment.PaymentBuilder paymentBuilder = Payment.builder()
                .amount(request.amount())
                .paymentMethod(request.paymentMethod().toUpperCase())
                .paymentType(request.paymentType().toUpperCase())
                .description(request.description())
                .customerName(request.customerName())
                .customerPhone(request.customerPhone())
                .vehicleNumber(request.vehicleNumber())
                .invoiceNumber(request.invoiceNumber())
                .paymentDate(request.paymentDate() != null ? request.paymentDate() : LocalDate.now())
                .garage(garage);

        if (request.jobCardId() != null) {
            JobCard jobCard = jobCardRepository.findById(request.jobCardId())
                    .orElseThrow(() -> new ResourceNotFoundException("Job card not found with id: " + request.jobCardId()));
            paymentBuilder.jobCard(jobCard);
        }

        Payment payment = paymentRepository.save(paymentBuilder.build());
        return mapToResponse(payment);
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        return mapToResponse(payment);
    }

    @Override
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<PaymentResponse> getPaymentsByGarageId(Long garageId) {
        return paymentRepository.findByGarageId(garageId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<PaymentResponse> getPaymentsByJobCardId(Long jobCardId) {
        return paymentRepository.findByJobCardId(jobCardId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public PaymentResponse updatePayment(Long id, PaymentRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        Garage garage = garageRepository.findById(request.garageId())
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + request.garageId()));

        payment.setAmount(request.amount());
        payment.setPaymentMethod(request.paymentMethod().toUpperCase());
        payment.setPaymentType(request.paymentType().toUpperCase());
        payment.setDescription(request.description());
        payment.setCustomerName(request.customerName());
        payment.setCustomerPhone(request.customerPhone());
        payment.setVehicleNumber(request.vehicleNumber());
        payment.setInvoiceNumber(request.invoiceNumber());
        payment.setPaymentDate(request.paymentDate() != null ? request.paymentDate() : payment.getPaymentDate());
        payment.setGarage(garage);

        if (request.jobCardId() != null) {
            JobCard jobCard = jobCardRepository.findById(request.jobCardId())
                    .orElseThrow(() -> new ResourceNotFoundException("Job card not found with id: " + request.jobCardId()));
            payment.setJobCard(jobCard);
        } else {
            payment.setJobCard(null);
        }

        payment = paymentRepository.save(payment);
        return mapToResponse(payment);
    }

    @Override
    @Transactional
    public void deletePayment(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Payment not found with id: " + id);
        }
        paymentRepository.deleteById(id);
    }

    @Override
    public List<PaymentResponse> getPaymentsByDateRange(Long garageId, LocalDate startDate, LocalDate endDate) {
        return paymentRepository.findByGarageIdAndPaymentDateBetween(garageId, startDate, endDate).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<PaymentResponse> getPaymentsByDate(Long garageId, LocalDate date) {
        return paymentRepository.findByGarageIdAndPaymentDate(garageId, date).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<PaymentResponse> getPaymentsByPaymentType(Long garageId, String paymentType) {
        return paymentRepository.findByGarageIdAndPaymentType(garageId, paymentType.toUpperCase()).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<PaymentResponse> getPaymentsByPaymentMethod(Long garageId, String paymentMethod) {
        return paymentRepository.findByGarageIdAndPaymentMethod(garageId, paymentMethod.toUpperCase()).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<PaymentResponse> getTodayPayments(Long garageId) {
        return paymentRepository.findByGarageIdAndPaymentDate(garageId, LocalDate.now()).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public RevenueResponse getRevenueReport(Long garageId) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + garageId));

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(7);
        LocalDate monthStart = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate yearStart = today.with(TemporalAdjusters.firstDayOfYear());

        BigDecimal totalRevenue = paymentRepository.getTotalRevenueByGarageId(garageId);
        BigDecimal todayRevenue = paymentRepository.getTodayRevenueByGarageId(garageId, today);
        BigDecimal weekRevenue = paymentRepository.getTotalRevenueByGarageIdAndDateRange(garageId, weekStart, today);
        BigDecimal monthRevenue = paymentRepository.getTotalRevenueByGarageIdAndDateRange(garageId, monthStart, today);
        BigDecimal yearRevenue = paymentRepository.getYearlyRevenueByGarageId(garageId, today.getYear());

        Long totalTransactions = paymentRepository.countByGarageId(garageId);
        Long todayTransactions = paymentRepository.countByGarageIdAndDateRange(garageId, today, today);

        // Revenue by payment type
        BigDecimal serviceRevenue = paymentRepository.getTotalRevenueByGarageIdAndPaymentType(garageId, "SERVICE");
        BigDecimal sparePartRevenue = paymentRepository.getTotalRevenueByGarageIdAndPaymentType(garageId, "SPARE_PART");
        BigDecimal lubeRevenue = paymentRepository.getTotalRevenueByGarageIdAndPaymentType(garageId, "LUBE");
        BigDecimal otherRevenue = paymentRepository.getTotalRevenueByGarageIdAndPaymentType(garageId, "OTHER");

        // Revenue by payment method
        BigDecimal cashRevenue = paymentRepository.getTotalRevenueByGarageIdAndPaymentMethod(garageId, "CASH");
        BigDecimal cardRevenue = paymentRepository.getTotalRevenueByGarageIdAndPaymentMethod(garageId, "CARD");
        BigDecimal upiRevenue = paymentRepository.getTotalRevenueByGarageIdAndPaymentMethod(garageId, "UPI");
        BigDecimal bankTransferRevenue = paymentRepository.getTotalRevenueByGarageIdAndPaymentMethod(garageId, "BANK_TRANSFER");

        return RevenueResponse.of(
                garageId,
                garage.getName(),
                totalRevenue,
                todayRevenue,
                weekRevenue,
                monthRevenue,
                yearRevenue,
                totalTransactions,
                todayTransactions,
                serviceRevenue,
                sparePartRevenue,
                lubeRevenue,
                otherRevenue,
                cashRevenue,
                cardRevenue,
                upiRevenue,
                bankTransferRevenue,
                null,
                null
        );
    }

    @Override
    public RevenueResponse getRevenueReportByDateRange(Long garageId, LocalDate startDate, LocalDate endDate) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + garageId));

        BigDecimal totalRevenue = paymentRepository.getTotalRevenueByGarageIdAndDateRange(garageId, startDate, endDate);
        Long totalTransactions = paymentRepository.countByGarageIdAndDateRange(garageId, startDate, endDate);

        // For date range report, we'll calculate specific metrics
        LocalDate today = LocalDate.now();
        BigDecimal todayRevenue = BigDecimal.ZERO;
        Long todayTransactions = 0L;

        if (!today.isBefore(startDate) && !today.isAfter(endDate)) {
            todayRevenue = paymentRepository.getTodayRevenueByGarageId(garageId, today);
            todayTransactions = paymentRepository.countByGarageIdAndDateRange(garageId, today, today);
        }

        // Revenue by payment type (within date range)
        BigDecimal serviceRevenue = paymentRepository.getTotalRevenueByGarageIdAndPaymentType(garageId, "SERVICE");
        BigDecimal sparePartRevenue = paymentRepository.getTotalRevenueByGarageIdAndPaymentType(garageId, "SPARE_PART");
        BigDecimal lubeRevenue = paymentRepository.getTotalRevenueByGarageIdAndPaymentType(garageId, "LUBE");
        BigDecimal otherRevenue = paymentRepository.getTotalRevenueByGarageIdAndPaymentType(garageId, "OTHER");

        // Revenue by payment method
        BigDecimal cashRevenue = paymentRepository.getTotalRevenueByGarageIdAndPaymentMethod(garageId, "CASH");
        BigDecimal cardRevenue = paymentRepository.getTotalRevenueByGarageIdAndPaymentMethod(garageId, "CARD");
        BigDecimal upiRevenue = paymentRepository.getTotalRevenueByGarageIdAndPaymentMethod(garageId, "UPI");
        BigDecimal bankTransferRevenue = paymentRepository.getTotalRevenueByGarageIdAndPaymentMethod(garageId, "BANK_TRANSFER");

        return RevenueResponse.of(
                garageId,
                garage.getName(),
                totalRevenue,
                todayRevenue,
                BigDecimal.ZERO,  // Week not applicable for custom range
                BigDecimal.ZERO,  // Month not applicable for custom range
                BigDecimal.ZERO,  // Year not applicable for custom range
                totalTransactions,
                todayTransactions,
                serviceRevenue,
                sparePartRevenue,
                lubeRevenue,
                otherRevenue,
                cashRevenue,
                cardRevenue,
                upiRevenue,
                bankTransferRevenue,
                startDate,
                endDate
        );
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getJobCard() != null ? payment.getJobCard().getId() : null,
                payment.getJobCard() != null ? payment.getJobCard().getVehicleNumberPlate() : null,
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentType(),
                payment.getDescription(),
                payment.getCustomerName(),
                payment.getCustomerPhone(),
                payment.getVehicleNumber(),
                payment.getInvoiceNumber(),
                payment.getPaymentDate(),
                payment.getCreatedAt(),
                payment.getGarage().getId(),
                payment.getGarage().getName()
        );
    }
}
