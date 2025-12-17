package com.garage_guru.dto.response;

/**
 * Response when a job card is completed, optionally with invoice generated
 */
public record JobCardCompletionResponse(
    JobCardResponse jobCard,
    InvoiceResponse invoice,
    String message
) {
    public static JobCardCompletionResponse withInvoice(JobCardResponse jobCard, InvoiceResponse invoice) {
        return new JobCardCompletionResponse(jobCard, invoice, "Job card completed and invoice generated successfully");
    }

    public static JobCardCompletionResponse withoutInvoice(JobCardResponse jobCard) {
        return new JobCardCompletionResponse(jobCard, null, "Job card completed successfully");
    }
}