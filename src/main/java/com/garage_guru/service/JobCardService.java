package com.garage_guru.service;

import com.garage_guru.dto.request.AddItemsToJobCardRequest;
import com.garage_guru.dto.request.CompleteJobCardRequest;
import com.garage_guru.dto.request.JobCardItemRequest;
import com.garage_guru.dto.request.JobCardRequest;
import com.garage_guru.dto.response.JobCardCompletionResponse;
import com.garage_guru.dto.response.JobCardResponse;

import java.util.List;

public interface JobCardService {

    // Basic CRUD
    JobCardResponse createJobCard(JobCardRequest request);
    JobCardResponse getJobCardById(Long id);
    List<JobCardResponse> getAllJobCards();
    List<JobCardResponse> getJobCardsByGarageId(Long garageId);
    JobCardResponse updateJobCard(Long id, JobCardRequest request);
    void deleteJobCard(Long id);

    // Status management
    List<JobCardResponse> getFlaggedJobCards(Long garageId);
    List<JobCardResponse> searchByVehicleNumber(String vehicleNumber);
    List<JobCardResponse> getCompletedJobCards(Long garageId);
    List<JobCardResponse> getPendingJobCards(Long garageId);
    List<JobCardResponse> getInProgressJobCards(Long garageId);
    List<JobCardResponse> getJobCardsByStatus(Long garageId, String status);
    JobCardResponse updateJobCardStatus(Long id, String status);

    // Item management
    JobCardResponse addItemToJobCard(Long jobCardId, JobCardItemRequest request);
    JobCardResponse addItemsToJobCard(Long jobCardId, AddItemsToJobCardRequest request);
    JobCardResponse removeItemFromJobCard(Long jobCardId, Long itemId);
    JobCardResponse updateJobCardItem(Long jobCardId, Long itemId, JobCardItemRequest request);

    // Complete job and generate invoice
    JobCardCompletionResponse completeJobCard(Long id, CompleteJobCardRequest request);
    JobCardResponse startJobCard(Long id);
}