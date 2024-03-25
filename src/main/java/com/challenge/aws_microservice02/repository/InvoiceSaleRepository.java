package com.challenge.aws_microservice02.repository;

import com.challenge.aws_microservice02.model.InvoiceSale;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceSaleRepository extends CrudRepository<InvoiceSale, Long> {
    Optional<InvoiceSale> findByInvoiceNumber(String invoiceNumber);

    List<InvoiceSale> findAllByCustomerName(String customerName);
}
