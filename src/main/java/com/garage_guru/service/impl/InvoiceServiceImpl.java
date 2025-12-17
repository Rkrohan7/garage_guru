package com.garage_guru.service.impl;

import com.garage_guru.dto.request.InvoiceItemRequest;
import com.garage_guru.dto.request.InvoicePaymentRequest;
import com.garage_guru.dto.request.InvoiceRequest;
import com.garage_guru.dto.response.InvoiceItemResponse;
import com.garage_guru.dto.response.InvoiceResponse;
import com.garage_guru.dto.response.InvoiceSummaryResponse;
import com.garage_guru.entity.Garage;
import com.garage_guru.entity.Invoice;
import com.garage_guru.entity.InvoiceItem;
import com.garage_guru.entity.JobCard;
import com.garage_guru.entity.JobCardItem;
import com.garage_guru.exception.ResourceNotFoundException;
import com.garage_guru.repository.GarageRepository;
import com.garage_guru.repository.InvoiceItemRepository;
import com.garage_guru.repository.InvoiceRepository;
import com.garage_guru.repository.JobCardRepository;
import com.garage_guru.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final GarageRepository garageRepository;
    private final JobCardRepository jobCardRepository;

    @Override
    @Transactional
    public InvoiceResponse createInvoice(InvoiceRequest request) {
        Garage garage = garageRepository.findById(request.garageId())
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + request.garageId()));

        JobCard jobCard = null;
        if (request.jobCardId() != null) {
            jobCard = jobCardRepository.findById(request.jobCardId())
                    .orElseThrow(() -> new ResourceNotFoundException("Job Card not found with id: " + request.jobCardId()));
        }

        String invoiceNumber = generateInvoiceNumber(garage.getId());

        Invoice invoice = Invoice.builder()
                .invoiceNumber(invoiceNumber)
                .garage(garage)
                .jobCard(jobCard)
                .customerName(request.customerName())
                .customerPhone(request.customerPhone())
                .customerEmail(request.customerEmail())
                .customerAddress(request.customerAddress())
                .vehicleNumber(request.vehicleNumber())
                .vehicleMake(request.vehicleMake())
                .vehicleModel(request.vehicleModel())
                .discountAmount(request.discountAmount() != null ? request.discountAmount() : BigDecimal.ZERO)
                .discountPercentage(request.discountPercentage() != null ? request.discountPercentage() : BigDecimal.ZERO)
                .taxAmount(request.taxAmount() != null ? request.taxAmount() : BigDecimal.ZERO)
                .taxPercentage(request.taxPercentage() != null ? request.taxPercentage() : BigDecimal.ZERO)
                .invoiceDate(request.invoiceDate() != null ? request.invoiceDate() : LocalDate.now())
                .dueDate(request.dueDate())
                .notes(request.notes())
                .termsAndConditions(request.termsAndConditions())
                .subtotal(BigDecimal.ZERO)
                .totalAmount(BigDecimal.ZERO)
                .items(new ArrayList<>())
                .build();

        // Add items if provided
        if (request.items() != null && !request.items().isEmpty()) {
            BigDecimal subtotal = BigDecimal.ZERO;
            for (InvoiceItemRequest itemRequest : request.items()) {
                InvoiceItem item = createInvoiceItem(itemRequest);
                invoice.addItem(item);
                subtotal = subtotal.add(item.getTotalPrice());
            }
            invoice.setSubtotal(subtotal);
        }

        invoice.calculateTotals();
        invoice = invoiceRepository.save(invoice);
        return mapToResponse(invoice);
    }

    @Override
    public InvoiceResponse getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
        return mapToResponse(invoice);
    }

    @Override
    public InvoiceResponse getInvoiceByNumber(String invoiceNumber) {
        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with number: " + invoiceNumber));
        return mapToResponse(invoice);
    }

    @Override
    public List<InvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<InvoiceResponse> getInvoicesByGarageId(Long garageId) {
        return invoiceRepository.findByGarageIdOrderByInvoiceDateDesc(garageId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public InvoiceResponse updateInvoice(Long id, InvoiceRequest request) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));

        Garage garage = garageRepository.findById(request.garageId())
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + request.garageId()));

        JobCard jobCard = null;
        if (request.jobCardId() != null) {
            jobCard = jobCardRepository.findById(request.jobCardId())
                    .orElseThrow(() -> new ResourceNotFoundException("Job Card not found with id: " + request.jobCardId()));
        }

        invoice.setGarage(garage);
        invoice.setJobCard(jobCard);
        invoice.setCustomerName(request.customerName());
        invoice.setCustomerPhone(request.customerPhone());
        invoice.setCustomerEmail(request.customerEmail());
        invoice.setCustomerAddress(request.customerAddress());
        invoice.setVehicleNumber(request.vehicleNumber());
        invoice.setVehicleMake(request.vehicleMake());
        invoice.setVehicleModel(request.vehicleModel());
        invoice.setDiscountAmount(request.discountAmount() != null ? request.discountAmount() : BigDecimal.ZERO);
        invoice.setDiscountPercentage(request.discountPercentage() != null ? request.discountPercentage() : BigDecimal.ZERO);
        invoice.setTaxAmount(request.taxAmount() != null ? request.taxAmount() : BigDecimal.ZERO);
        invoice.setTaxPercentage(request.taxPercentage() != null ? request.taxPercentage() : BigDecimal.ZERO);
        invoice.setInvoiceDate(request.invoiceDate() != null ? request.invoiceDate() : invoice.getInvoiceDate());
        invoice.setDueDate(request.dueDate());
        invoice.setNotes(request.notes());
        invoice.setTermsAndConditions(request.termsAndConditions());

        // Update items
        if (request.items() != null) {
            invoice.getItems().clear();
            BigDecimal subtotal = BigDecimal.ZERO;
            for (InvoiceItemRequest itemRequest : request.items()) {
                InvoiceItem item = createInvoiceItem(itemRequest);
                invoice.addItem(item);
                subtotal = subtotal.add(item.getTotalPrice());
            }
            invoice.setSubtotal(subtotal);
        }

        invoice.calculateTotals();
        invoice = invoiceRepository.save(invoice);
        return mapToResponse(invoice);
    }

    @Override
    @Transactional
    public void deleteInvoice(Long id) {
        if (!invoiceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Invoice not found with id: " + id);
        }
        invoiceRepository.deleteById(id);
    }

    @Override
    @Transactional
    public InvoiceResponse createInvoiceFromJobCard(Long jobCardId) {
        JobCard jobCard = jobCardRepository.findById(jobCardId)
                .orElseThrow(() -> new ResourceNotFoundException("Job Card not found with id: " + jobCardId));

        String invoiceNumber = generateInvoiceNumber(jobCard.getGarage().getId());

        Invoice invoice = Invoice.builder()
                .invoiceNumber(invoiceNumber)
                .garage(jobCard.getGarage())
                .jobCard(jobCard)
                .customerName(jobCard.getCustomerName())
                .customerPhone(jobCard.getCustomerPhone())
                .vehicleNumber(jobCard.getVehicleNumberPlate())
                .vehicleMake(jobCard.getMake())
                .vehicleModel(jobCard.getModel())
                .invoiceDate(LocalDate.now())
                .subtotal(BigDecimal.ZERO)
                .totalAmount(BigDecimal.ZERO)
                .items(new ArrayList<>())
                .build();

        invoice.calculateTotals();
        invoice = invoiceRepository.save(invoice);
        return mapToResponse(invoice);
    }

    @Override
    @Transactional
    public InvoiceResponse createInvoiceFromCompletedJobCard(Long jobCardId, BigDecimal invoiceDiscountAmount,
            BigDecimal invoiceTaxPercentage, String invoiceNotes, String termsAndConditions, Integer dueDays) {

        JobCard jobCard = jobCardRepository.findById(jobCardId)
                .orElseThrow(() -> new ResourceNotFoundException("Job Card not found with id: " + jobCardId));

        String invoiceNumber = generateInvoiceNumber(jobCard.getGarage().getId());

        // Calculate due date
        LocalDate dueDate = dueDays != null ? LocalDate.now().plusDays(dueDays) : null;

        Invoice invoice = Invoice.builder()
                .invoiceNumber(invoiceNumber)
                .garage(jobCard.getGarage())
                .jobCard(jobCard)
                .customerName(jobCard.getCustomerName())
                .customerPhone(jobCard.getCustomerPhone())
                .customerEmail(jobCard.getCustomerEmail())
                .customerAddress(jobCard.getCustomerAddress())
                .vehicleNumber(jobCard.getVehicleNumberPlate())
                .vehicleMake(jobCard.getMake())
                .vehicleModel(jobCard.getModel())
                .invoiceDate(LocalDate.now())
                .dueDate(dueDate)
                .notes(invoiceNotes)
                .termsAndConditions(termsAndConditions)
                .discountAmount(invoiceDiscountAmount != null ? invoiceDiscountAmount : jobCard.getDiscountAmount())
                .taxPercentage(invoiceTaxPercentage != null ? invoiceTaxPercentage : BigDecimal.ZERO)
                .taxAmount(jobCard.getTaxAmount())
                .subtotal(BigDecimal.ZERO)
                .totalAmount(BigDecimal.ZERO)
                .status("SENT")
                .items(new ArrayList<>())
                .build();

        // Copy all items from job card to invoice
        BigDecimal subtotal = BigDecimal.ZERO;
        for (JobCardItem jobCardItem : jobCard.getItems()) {
            InvoiceItem invoiceItem = InvoiceItem.builder()
                    .itemType(jobCardItem.getItemType())
                    .itemName(jobCardItem.getItemName())
                    .itemCode(jobCardItem.getItemCode())
                    .description(jobCardItem.getDescription())
                    .quantity(jobCardItem.getQuantity())
                    .unitPrice(jobCardItem.getUnitPrice())
                    .discountAmount(jobCardItem.getDiscountAmount())
                    .taxAmount(jobCardItem.getTaxAmount())
                    .totalPrice(jobCardItem.getTotalPrice())
                    .sparePartStockId(jobCardItem.getSparePartStockId())
                    .lubesStockId(jobCardItem.getLubesStockId())
                    .serviceStockId(jobCardItem.getServiceStockId())
                    .build();
            invoice.addItem(invoiceItem);
            subtotal = subtotal.add(invoiceItem.getTotalPrice());
        }

        // Add labour charges as a separate item if present
        if (jobCard.getLabourCharges() != null && jobCard.getLabourCharges().compareTo(BigDecimal.ZERO) > 0) {
            InvoiceItem labourItem = InvoiceItem.builder()
                    .itemType("LABOUR")
                    .itemName("Labour Charges")
                    .description("Labour charges for service")
                    .quantity(1)
                    .unitPrice(jobCard.getLabourCharges())
                    .discountAmount(BigDecimal.ZERO)
                    .taxAmount(BigDecimal.ZERO)
                    .totalPrice(jobCard.getLabourCharges())
                    .build();
            invoice.addItem(labourItem);
            subtotal = subtotal.add(jobCard.getLabourCharges());
        }

        invoice.setSubtotal(subtotal);
        invoice.calculateTotals();
        invoice = invoiceRepository.save(invoice);
        return mapToResponse(invoice);
    }

    @Override
    @Transactional
    public InvoiceResponse updateInvoiceStatus(Long id, String status) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));

        invoice.setStatus(status.toUpperCase());
        invoice = invoiceRepository.save(invoice);
        return mapToResponse(invoice);
    }

    @Override
    @Transactional
    public InvoiceResponse markAsSent(Long id) {
        return updateInvoiceStatus(id, "SENT");
    }

    @Override
    @Transactional
    public InvoiceResponse markAsPaid(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));

        invoice.setStatus("PAID");
        invoice.setPaymentStatus("PAID");
        invoice.setPaidAmount(invoice.getTotalAmount());
        invoice.setDueAmount(BigDecimal.ZERO);
        invoice.setPaidDate(LocalDate.now());

        invoice = invoiceRepository.save(invoice);
        return mapToResponse(invoice);
    }

    @Override
    @Transactional
    public InvoiceResponse cancelInvoice(Long id) {
        return updateInvoiceStatus(id, "CANCELLED");
    }

    @Override
    @Transactional
    public InvoiceResponse addPayment(Long id, InvoicePaymentRequest request) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));

        BigDecimal newPaidAmount = invoice.getPaidAmount().add(request.amount());
        invoice.setPaidAmount(newPaidAmount);
        invoice.calculateTotals();

        if (invoice.getPaymentStatus().equals("PAID")) {
            invoice.setStatus("PAID");
            invoice.setPaidDate(request.paymentDate() != null ? request.paymentDate() : LocalDate.now());
        } else {
            invoice.setStatus("PARTIALLY_PAID");
        }

        invoice = invoiceRepository.save(invoice);
        return mapToResponse(invoice);
    }

    @Override
    public List<InvoiceResponse> getInvoicesByStatus(Long garageId, String status) {
        return invoiceRepository.findByGarageIdAndStatus(garageId, status.toUpperCase()).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<InvoiceResponse> getInvoicesByPaymentStatus(Long garageId, String paymentStatus) {
        return invoiceRepository.findByGarageIdAndPaymentStatus(garageId, paymentStatus.toUpperCase()).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<InvoiceResponse> getInvoicesByDateRange(Long garageId, LocalDate startDate, LocalDate endDate) {
        return invoiceRepository.findByGarageIdAndInvoiceDateBetween(garageId, startDate, endDate).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<InvoiceResponse> getInvoicesByDate(Long garageId, LocalDate date) {
        return invoiceRepository.findByGarageIdAndInvoiceDate(garageId, date).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<InvoiceResponse> getTodayInvoices(Long garageId) {
        return invoiceRepository.findByGarageIdAndInvoiceDate(garageId, LocalDate.now()).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<InvoiceResponse> getOverdueInvoices(Long garageId) {
        return invoiceRepository.findOverdueInvoices(garageId, LocalDate.now()).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<InvoiceResponse> searchByCustomerName(Long garageId, String customerName) {
        return invoiceRepository.findByGarageIdAndCustomerNameContainingIgnoreCase(garageId, customerName).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<InvoiceResponse> searchByVehicleNumber(Long garageId, String vehicleNumber) {
        return invoiceRepository.findByGarageIdAndVehicleNumberContainingIgnoreCase(garageId, vehicleNumber).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<InvoiceResponse> getInvoicesByJobCard(Long jobCardId) {
        return invoiceRepository.findByJobCardId(jobCardId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public InvoiceSummaryResponse getInvoiceSummary(Long garageId) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + garageId));

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(7);
        LocalDate monthStart = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate yearStart = today.with(TemporalAdjusters.firstDayOfYear());

        Long totalInvoices = invoiceRepository.countByGarageId(garageId);
        Long draftInvoices = invoiceRepository.countByGarageIdAndStatus(garageId, "DRAFT");
        Long sentInvoices = invoiceRepository.countByGarageIdAndStatus(garageId, "SENT");
        Long paidInvoices = invoiceRepository.countByGarageIdAndPaymentStatus(garageId, "PAID");
        Long partiallyPaidInvoices = invoiceRepository.countByGarageIdAndPaymentStatus(garageId, "PARTIAL");
        Long overdueInvoices = invoiceRepository.countOverdueInvoices(garageId, today);
        Long cancelledInvoices = invoiceRepository.countByGarageIdAndStatus(garageId, "CANCELLED");

        BigDecimal totalInvoiceAmount = invoiceRepository.getTotalInvoiceAmountByGarageId(garageId);
        BigDecimal totalPaidAmount = invoiceRepository.getTotalPaidAmountByGarageId(garageId);
        BigDecimal totalDueAmount = invoiceRepository.getTotalDueAmountByGarageId(garageId);
        BigDecimal totalOverdueAmount = invoiceRepository.getTotalOverdueAmount(garageId, today);

        BigDecimal todayInvoiceTotal = invoiceRepository.getTodayInvoiceTotalByGarageId(garageId, today);
        BigDecimal weekInvoiceTotal = invoiceRepository.getTotalInvoiceAmountByGarageIdAndDateRange(garageId, weekStart, today);
        BigDecimal monthInvoiceTotal = invoiceRepository.getTotalInvoiceAmountByGarageIdAndDateRange(garageId, monthStart, today);
        BigDecimal yearInvoiceTotal = invoiceRepository.getYearlyInvoiceTotalByGarageId(garageId, today.getYear());

        return InvoiceSummaryResponse.of(
                garageId, garage.getName(),
                totalInvoices, draftInvoices, sentInvoices, paidInvoices, partiallyPaidInvoices, overdueInvoices, cancelledInvoices,
                totalInvoiceAmount, totalPaidAmount, totalDueAmount, totalOverdueAmount,
                todayInvoiceTotal, weekInvoiceTotal, monthInvoiceTotal, yearInvoiceTotal,
                null, null
        );
    }

    @Override
    public InvoiceSummaryResponse getInvoiceSummaryByDateRange(Long garageId, LocalDate startDate, LocalDate endDate) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + garageId));

        LocalDate today = LocalDate.now();

        Long totalInvoices = invoiceRepository.countByGarageIdAndDateRange(garageId, startDate, endDate);
        Long draftInvoices = invoiceRepository.countByGarageIdAndStatus(garageId, "DRAFT");
        Long sentInvoices = invoiceRepository.countByGarageIdAndStatus(garageId, "SENT");
        Long paidInvoices = invoiceRepository.countByGarageIdAndPaymentStatus(garageId, "PAID");
        Long partiallyPaidInvoices = invoiceRepository.countByGarageIdAndPaymentStatus(garageId, "PARTIAL");
        Long overdueInvoices = invoiceRepository.countOverdueInvoices(garageId, today);
        Long cancelledInvoices = invoiceRepository.countByGarageIdAndStatus(garageId, "CANCELLED");

        BigDecimal totalInvoiceAmount = invoiceRepository.getTotalInvoiceAmountByGarageIdAndDateRange(garageId, startDate, endDate);
        BigDecimal totalPaidAmount = invoiceRepository.getTotalPaidAmountByGarageId(garageId);
        BigDecimal totalDueAmount = invoiceRepository.getTotalDueAmountByGarageId(garageId);
        BigDecimal totalOverdueAmount = invoiceRepository.getTotalOverdueAmount(garageId, today);

        BigDecimal todayInvoiceTotal = BigDecimal.ZERO;
        if (!today.isBefore(startDate) && !today.isAfter(endDate)) {
            todayInvoiceTotal = invoiceRepository.getTodayInvoiceTotalByGarageId(garageId, today);
        }

        return InvoiceSummaryResponse.of(
                garageId, garage.getName(),
                totalInvoices, draftInvoices, sentInvoices, paidInvoices, partiallyPaidInvoices, overdueInvoices, cancelledInvoices,
                totalInvoiceAmount, totalPaidAmount, totalDueAmount, totalOverdueAmount,
                todayInvoiceTotal, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                startDate, endDate
        );
    }

    private String generateInvoiceNumber(Long garageId) {
        String prefix = "INV-" + garageId + "-";
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String maxNumber = invoiceRepository.getMaxInvoiceNumberByGarageId(garageId);

        int sequence = 1;
        if (maxNumber != null && maxNumber.contains(datePart)) {
            String[] parts = maxNumber.split("-");
            if (parts.length > 3) {
                try {
                    sequence = Integer.parseInt(parts[3]) + 1;
                } catch (NumberFormatException e) {
                    sequence = 1;
                }
            }
        }

        return prefix + datePart + "-" + String.format("%04d", sequence);
    }

    private InvoiceItem createInvoiceItem(InvoiceItemRequest request) {
        BigDecimal unitPrice = request.unitPrice() != null ? request.unitPrice() : BigDecimal.ZERO;
        Integer quantity = request.quantity() != null ? request.quantity() : 1;
        BigDecimal discountAmount = request.discountAmount() != null ? request.discountAmount() : BigDecimal.ZERO;
        BigDecimal taxAmount = request.taxAmount() != null ? request.taxAmount() : BigDecimal.ZERO;
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity)).subtract(discountAmount).add(taxAmount);

        return InvoiceItem.builder()
                .itemType(request.itemType().toUpperCase())
                .itemName(request.itemName())
                .itemCode(request.itemCode())
                .description(request.description())
                .quantity(quantity)
                .unitPrice(unitPrice)
                .discountAmount(discountAmount)
                .taxAmount(taxAmount)
                .totalPrice(totalPrice)
                .sparePartStockId(request.sparePartStockId())
                .lubesStockId(request.lubesStockId())
                .serviceStockId(request.serviceStockId())
                .build();
    }

    private InvoiceResponse mapToResponse(Invoice invoice) {
        List<InvoiceItemResponse> itemResponses = invoice.getItems().stream()
                .map(this::mapItemToResponse)
                .toList();

        return new InvoiceResponse(
                invoice.getId(),
                invoice.getInvoiceNumber(),
                invoice.getJobCard() != null ? invoice.getJobCard().getId() : null,
                invoice.getGarage().getId(),
                invoice.getGarage().getName(),
                invoice.getCustomerName(),
                invoice.getCustomerPhone(),
                invoice.getCustomerEmail(),
                invoice.getCustomerAddress(),
                invoice.getVehicleNumber(),
                invoice.getVehicleMake(),
                invoice.getVehicleModel(),
                itemResponses,
                itemResponses.size(),
                invoice.getSubtotal(),
                invoice.getDiscountAmount(),
                invoice.getDiscountPercentage(),
                invoice.getTaxAmount(),
                invoice.getTaxPercentage(),
                invoice.getTotalAmount(),
                invoice.getPaidAmount(),
                invoice.getDueAmount(),
                invoice.getStatus(),
                invoice.getPaymentStatus(),
                invoice.getInvoiceDate(),
                invoice.getDueDate(),
                invoice.getPaidDate(),
                invoice.getNotes(),
                invoice.getTermsAndConditions(),
                invoice.getCreatedAt(),
                invoice.getUpdatedAt()
        );
    }

    private InvoiceItemResponse mapItemToResponse(InvoiceItem item) {
        return new InvoiceItemResponse(
                item.getId(),
                item.getItemType(),
                item.getItemName(),
                item.getItemCode(),
                item.getDescription(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getDiscountAmount(),
                item.getTaxAmount(),
                item.getTotalPrice(),
                item.getSparePartStockId(),
                item.getLubesStockId(),
                item.getServiceStockId()
        );
    }
}