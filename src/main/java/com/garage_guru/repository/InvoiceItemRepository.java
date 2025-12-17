package com.garage_guru.repository;

import com.garage_guru.entity.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {

    List<InvoiceItem> findByInvoiceId(Long invoiceId);

    List<InvoiceItem> findByInvoiceIdAndItemType(Long invoiceId, String itemType);

    void deleteByInvoiceId(Long invoiceId);
}