package com.garage_guru.service.impl;

import com.garage_guru.dto.request.AddItemsToJobCardRequest;
import com.garage_guru.dto.request.CompleteJobCardRequest;
import com.garage_guru.dto.request.JobCardItemRequest;
import com.garage_guru.dto.request.JobCardRequest;
import com.garage_guru.dto.response.InvoiceResponse;
import com.garage_guru.dto.response.JobCardCompletionResponse;
import com.garage_guru.dto.response.JobCardItemResponse;
import com.garage_guru.dto.response.JobCardResponse;
import com.garage_guru.entity.Garage;
import com.garage_guru.entity.JobCard;
import com.garage_guru.entity.JobCardItem;
import com.garage_guru.exception.ResourceNotFoundException;
import com.garage_guru.repository.GarageRepository;
import com.garage_guru.repository.JobCardItemRepository;
import com.garage_guru.repository.JobCardRepository;
import com.garage_guru.repository.SparePartStockRepository;
import com.garage_guru.repository.LubesStockRepository;
import com.garage_guru.repository.ServiceStockRepository;
import com.garage_guru.service.JobCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JobCardServiceImpl implements JobCardService {

    private final JobCardRepository jobCardRepository;
    private final JobCardItemRepository jobCardItemRepository;
    private final GarageRepository garageRepository;
    private final SparePartStockRepository sparePartStockRepository;
    private final LubesStockRepository lubesStockRepository;
    private final ServiceStockRepository serviceStockRepository;

    @Override
    @Transactional
    public JobCardResponse createJobCard(JobCardRequest request) {
        Garage garage = garageRepository.findById(request.garageId())
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + request.garageId()));

        JobCard jobCard = JobCard.builder()
                .vehicleType(request.vehicleType())
                .vehicleNumberPlate(request.vehicleNumberPlate())
                .make(request.make())
                .model(request.model())
                .variant(request.variant())
                .kmReading(request.kmReading())
                .fuelLevel(request.fuelLevel())
                .jobNotes(request.jobNotes())
                .flag(Objects.requireNonNullElse(request.flag(), false))
                .status(Objects.requireNonNullElse(request.status(), "PENDING"))
                .customerName(request.customerName())
                .customerPhone(request.customerPhone())
                .customerEmail(request.customerEmail())
                .customerAddress(request.customerAddress())
                .labourCharges(request.labourCharges() != null ? request.labourCharges() : BigDecimal.ZERO)
                .discountAmount(request.discountAmount() != null ? request.discountAmount() : BigDecimal.ZERO)
                .taxAmount(request.taxAmount() != null ? request.taxAmount() : BigDecimal.ZERO)
                .garage(garage)
                .items(new ArrayList<>())
                .build();

        jobCard = jobCardRepository.save(jobCard);
        return mapToResponse(jobCard);
    }

    @Override
    public JobCardResponse getJobCardById(Long id) {
        JobCard jobCard = jobCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job card not found with id: " + id));
        return mapToResponse(jobCard);
    }

    @Override
    public List<JobCardResponse> getAllJobCards() {
        return jobCardRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<JobCardResponse> getJobCardsByGarageId(Long garageId) {
        return jobCardRepository.findByGarageId(garageId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<JobCardResponse> getFlaggedJobCards(Long garageId) {
        return jobCardRepository.findByGarageIdAndFlag(garageId, true).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<JobCardResponse> searchByVehicleNumber(String vehicleNumber) {
        return jobCardRepository.findByVehicleNumberPlateContainingIgnoreCase(vehicleNumber).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<JobCardResponse> getCompletedJobCards(Long garageId) {
        return jobCardRepository.findByGarageIdAndStatus(garageId, "COMPLETED").stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<JobCardResponse> getPendingJobCards(Long garageId) {
        return jobCardRepository.findByGarageIdAndStatus(garageId, "PENDING").stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<JobCardResponse> getInProgressJobCards(Long garageId) {
        return jobCardRepository.findByGarageIdAndStatus(garageId, "IN_PROGRESS").stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<JobCardResponse> getJobCardsByStatus(Long garageId, String status) {
        return jobCardRepository.findByGarageIdAndStatus(garageId, status.toUpperCase()).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public JobCardResponse updateJobCardStatus(Long id, String status) {
        JobCard jobCard = jobCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job card not found with id: " + id));

        jobCard.setStatus(status.toUpperCase());
        if ("COMPLETED".equals(status.toUpperCase())) {
            jobCard.setCompletedAt(LocalDateTime.now());
        }
        jobCard = jobCardRepository.save(jobCard);
        return mapToResponse(jobCard);
    }

    @Override
    @Transactional
    public JobCardResponse updateJobCard(Long id, JobCardRequest request) {
        JobCard jobCard = jobCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job card not found with id: " + id));

        Garage garage = garageRepository.findById(request.garageId())
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + request.garageId()));

        jobCard.setVehicleType(request.vehicleType());
        jobCard.setVehicleNumberPlate(request.vehicleNumberPlate());
        jobCard.setMake(request.make());
        jobCard.setModel(request.model());
        jobCard.setVariant(request.variant());
        jobCard.setKmReading(request.kmReading());
        jobCard.setFuelLevel(request.fuelLevel());
        jobCard.setJobNotes(request.jobNotes());
        jobCard.setFlag(Objects.requireNonNullElse(request.flag(), jobCard.getFlag()));
        jobCard.setStatus(Objects.requireNonNullElse(request.status(), jobCard.getStatus()));
        jobCard.setCustomerName(request.customerName());
        jobCard.setCustomerPhone(request.customerPhone());
        jobCard.setCustomerEmail(request.customerEmail());
        jobCard.setCustomerAddress(request.customerAddress());
        jobCard.setLabourCharges(request.labourCharges() != null ? request.labourCharges() : jobCard.getLabourCharges());
        jobCard.setDiscountAmount(request.discountAmount() != null ? request.discountAmount() : jobCard.getDiscountAmount());
        jobCard.setTaxAmount(request.taxAmount() != null ? request.taxAmount() : jobCard.getTaxAmount());
        jobCard.setGarage(garage);

        jobCard.calculateTotals();
        jobCard = jobCardRepository.save(jobCard);
        return mapToResponse(jobCard);
    }

    @Override
    @Transactional
    public void deleteJobCard(Long id) {
        if (!jobCardRepository.existsById(id)) {
            throw new ResourceNotFoundException("Job card not found with id: " + id);
        }
        jobCardRepository.deleteById(id);
    }

    @Override
    @Transactional
    public JobCardResponse addItemToJobCard(Long jobCardId, JobCardItemRequest request) {
        JobCard jobCard = jobCardRepository.findById(jobCardId)
                .orElseThrow(() -> new ResourceNotFoundException("Job card not found with id: " + jobCardId));

        JobCardItem item = createJobCardItem(request);
        item.setJobCard(jobCard);
        item.setCreatedAt(LocalDateTime.now());

        // Save item first
        item = jobCardItemRepository.save(item);

        // Add to list and recalculate
        jobCard.getItems().add(item);
        jobCard.calculateTotals();

        // Deduct from stock if applicable
        deductFromStock(request);

        jobCard = jobCardRepository.save(jobCard);
        return mapToResponse(jobCard);
    }

    @Override
    @Transactional
    public JobCardResponse addItemsToJobCard(Long jobCardId, AddItemsToJobCardRequest request) {
        JobCard jobCard = jobCardRepository.findById(jobCardId)
                .orElseThrow(() -> new ResourceNotFoundException("Job card not found with id: " + jobCardId));

        for (JobCardItemRequest itemRequest : request.items()) {
            JobCardItem item = createJobCardItem(itemRequest);
            item.setJobCard(jobCard);
            item.setCreatedAt(LocalDateTime.now());
            item = jobCardItemRepository.save(item);
            jobCard.getItems().add(item);
            deductFromStock(itemRequest);
        }

        if (request.labourCharges() != null) {
            jobCard.setLabourCharges(request.labourCharges());
        }
        if (request.discountAmount() != null) {
            jobCard.setDiscountAmount(request.discountAmount());
        }
        if (request.taxAmount() != null) {
            jobCard.setTaxAmount(request.taxAmount());
        }

        jobCard.calculateTotals();
        jobCard = jobCardRepository.save(jobCard);
        return mapToResponse(jobCard);
    }

    @Override
    @Transactional
    public JobCardResponse removeItemFromJobCard(Long jobCardId, Long itemId) {
        JobCard jobCard = jobCardRepository.findById(jobCardId)
                .orElseThrow(() -> new ResourceNotFoundException("Job card not found with id: " + jobCardId));

        JobCardItem item = jobCardItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + itemId));

        if (!item.getJobCard().getId().equals(jobCardId)) {
            throw new ResourceNotFoundException("Item does not belong to this job card");
        }

        // Add back to stock if applicable
        addBackToStock(item);

        jobCard.removeItem(item);
        jobCardItemRepository.delete(item);
        jobCard = jobCardRepository.save(jobCard);
        return mapToResponse(jobCard);
    }

    @Override
    @Transactional
    public JobCardResponse updateJobCardItem(Long jobCardId, Long itemId, JobCardItemRequest request) {
        JobCard jobCard = jobCardRepository.findById(jobCardId)
                .orElseThrow(() -> new ResourceNotFoundException("Job card not found with id: " + jobCardId));

        JobCardItem item = jobCardItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + itemId));

        if (!item.getJobCard().getId().equals(jobCardId)) {
            throw new ResourceNotFoundException("Item does not belong to this job card");
        }

        // Add back old quantity to stock
        addBackToStock(item);

        // Update item
        item.setItemType(request.itemType().toUpperCase());
        item.setItemName(request.itemName());
        item.setItemCode(request.itemCode());
        item.setDescription(request.description());
        item.setQuantity(request.quantity());
        item.setUnitPrice(request.unitPrice());
        item.setDiscountAmount(request.discountAmount() != null ? request.discountAmount() : BigDecimal.ZERO);
        item.setTaxPercentage(request.taxPercentage() != null ? request.taxPercentage() : BigDecimal.ZERO);
        item.setTaxAmount(request.taxAmount() != null ? request.taxAmount() : BigDecimal.ZERO);
        item.setSparePartStockId(request.sparePartStockId());
        item.setLubesStockId(request.lubesStockId());
        item.setServiceStockId(request.serviceStockId());
        item.calculateTotal();

        // Deduct new quantity from stock
        deductFromStock(request);

        jobCardItemRepository.save(item);
        jobCard.calculateTotals();
        jobCard = jobCardRepository.save(jobCard);
        return mapToResponse(jobCard);
    }

    @Override
    @Transactional
    public JobCardResponse startJobCard(Long id) {
        JobCard jobCard = jobCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job card not found with id: " + id));

        jobCard.setStatus("IN_PROGRESS");
        jobCard = jobCardRepository.save(jobCard);
        return mapToResponse(jobCard);
    }

    @Override
    @Transactional
    public JobCardCompletionResponse completeJobCard(Long id, CompleteJobCardRequest request) {
        JobCard jobCard = jobCardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job card not found with id: " + id));

        // Add items if provided
        if (request.items() != null && !request.items().isEmpty()) {
            for (JobCardItemRequest itemRequest : request.items()) {
                JobCardItem item = createJobCardItem(itemRequest);
                item.setJobCard(jobCard);
                item.setCreatedAt(LocalDateTime.now());
                item = jobCardItemRepository.save(item);
                jobCard.getItems().add(item);
                deductFromStock(itemRequest);
            }
        }

        // Update pricing
        if (request.labourCharges() != null) {
            jobCard.setLabourCharges(request.labourCharges());
        }
        if (request.discountAmount() != null) {
            jobCard.setDiscountAmount(request.discountAmount());
        }
        if (request.taxAmount() != null) {
            jobCard.setTaxAmount(request.taxAmount());
        }
        if (request.notes() != null) {
            jobCard.setJobNotes(request.notes());
        }

        // Mark as completed
        jobCard.setStatus("COMPLETED");
        jobCard.setCompletedAt(LocalDateTime.now());
        jobCard.calculateTotals();
        jobCard = jobCardRepository.save(jobCard);

        JobCardResponse jobCardResponse = mapToResponse(jobCard);

        // Generate invoice if requested
        if (Boolean.TRUE.equals(request.generateInvoice())) {
            InvoiceResponse invoiceResponse = generateInvoiceFromJobCard(jobCard, request);
            return JobCardCompletionResponse.withInvoice(jobCardResponse, invoiceResponse);
        }

        return JobCardCompletionResponse.withoutInvoice(jobCardResponse);
    }

    private InvoiceResponse generateInvoiceFromJobCard(JobCard jobCard, CompleteJobCardRequest request) {
        // This will be handled by InvoiceService - we'll inject it
        // For now, return null and handle in controller
        return null;
    }

    private JobCardItem createJobCardItem(JobCardItemRequest request) {
        BigDecimal unitPrice = request.unitPrice() != null ? request.unitPrice() : BigDecimal.ZERO;
        Integer quantity = request.quantity() != null ? request.quantity() : 1;
        BigDecimal discountAmount = request.discountAmount() != null ? request.discountAmount() : BigDecimal.ZERO;
        BigDecimal taxPercentage = request.taxPercentage() != null ? request.taxPercentage() : BigDecimal.ZERO;
        BigDecimal taxAmount = request.taxAmount() != null ? request.taxAmount() : BigDecimal.ZERO;
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity)).subtract(discountAmount).add(taxAmount);

        return JobCardItem.builder()
                .itemType(request.itemType().toUpperCase())
                .itemName(request.itemName())
                .itemCode(request.itemCode())
                .description(request.description())
                .quantity(quantity)
                .unitPrice(unitPrice)
                .discountAmount(discountAmount)
                .taxPercentage(taxPercentage)
                .taxAmount(taxAmount)
                .totalPrice(totalPrice)
                .sparePartStockId(request.sparePartStockId())
                .lubesStockId(request.lubesStockId())
                .serviceStockId(request.serviceStockId())
                .build();
    }

    private void deductFromStock(JobCardItemRequest request) {
        if (request.sparePartStockId() != null) {
            sparePartStockRepository.findById(request.sparePartStockId()).ifPresent(stock -> {
                stock.setStockQuantity(stock.getStockQuantity() - request.quantity());
                sparePartStockRepository.save(stock);
            });
        }
        if (request.lubesStockId() != null) {
            lubesStockRepository.findById(request.lubesStockId()).ifPresent(stock -> {
                stock.setStockQuantity(stock.getStockQuantity() - request.quantity());
                lubesStockRepository.save(stock);
            });
        }
    }

    private void addBackToStock(JobCardItem item) {
        if (item.getSparePartStockId() != null) {
            sparePartStockRepository.findById(item.getSparePartStockId()).ifPresent(stock -> {
                stock.setStockQuantity(stock.getStockQuantity() + item.getQuantity());
                sparePartStockRepository.save(stock);
            });
        }
        if (item.getLubesStockId() != null) {
            lubesStockRepository.findById(item.getLubesStockId()).ifPresent(stock -> {
                stock.setStockQuantity(stock.getStockQuantity() + item.getQuantity());
                lubesStockRepository.save(stock);
            });
        }
    }

    private JobCardResponse mapToResponse(JobCard jobCard) {
        List<JobCardItemResponse> itemResponses = jobCard.getItems().stream()
                .map(this::mapItemToResponse)
                .toList();

        return new JobCardResponse(
                jobCard.getId(),
                jobCard.getVehicleType(),
                jobCard.getVehicleNumberPlate(),
                jobCard.getMake(),
                jobCard.getModel(),
                jobCard.getVariant(),
                jobCard.getKmReading(),
                jobCard.getFuelLevel(),
                jobCard.getJobNotes(),
                jobCard.getFlag(),
                jobCard.getStatus(),
                jobCard.getCustomerName(),
                jobCard.getCustomerPhone(),
                jobCard.getCustomerEmail(),
                jobCard.getCustomerAddress(),
                jobCard.getGarage().getId(),
                jobCard.getGarage().getName(),
                itemResponses,
                itemResponses.size(),
                jobCard.getSubtotal(),
                jobCard.getLabourCharges(),
                jobCard.getDiscountAmount(),
                jobCard.getTaxAmount(),
                jobCard.getTotalAmount(),
                jobCard.getCreatedAt(),
                jobCard.getUpdatedAt(),
                jobCard.getCompletedAt()
        );
    }

    private JobCardItemResponse mapItemToResponse(JobCardItem item) {
        return new JobCardItemResponse(
                item.getId(),
                item.getItemType(),
                item.getItemName(),
                item.getItemCode(),
                item.getDescription(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getDiscountAmount(),
                item.getTaxPercentage(),
                item.getTaxAmount(),
                item.getTotalPrice(),
                item.getSparePartStockId(),
                item.getLubesStockId(),
                item.getServiceStockId(),
                item.getCreatedAt()
        );
    }
}
