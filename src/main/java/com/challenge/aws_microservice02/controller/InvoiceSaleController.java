package com.challenge.aws_microservice02.controller;

import com.challenge.aws_microservice02.model.InvoiceSale;
import com.challenge.aws_microservice02.model.UrlResponse;
import com.challenge.aws_microservice02.repository.InvoiceSaleRepository;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceSaleController {

    @Value("${aws.s3.bucket.invoice.name}")
    private String bucketName;

    private AmazonS3 amazonS3;
    private InvoiceSaleRepository invoiceRepository;

    @Autowired
    public InvoiceSaleController(AmazonS3 amazonS3, InvoiceSaleRepository invoiceRepository) {
        this.amazonS3 = amazonS3;
        this.invoiceRepository = invoiceRepository;
    }

    @PostMapping
    public ResponseEntity<UrlResponse> createInvoiceUrl() {
        UrlResponse urlResponse  = new UrlResponse();
        Instant expirationTime = Instant.now().plus(Duration.ofMinutes(5));
        String processId = UUID.randomUUID().toString();

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, processId)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(Date.from(expirationTime));

        urlResponse.setExpirationTime(expirationTime.getEpochSecond());
        urlResponse.setUrl(amazonS3.generatePresignedUrl(
                generatePresignedUrlRequest).toString());

        return new ResponseEntity<UrlResponse>(urlResponse, HttpStatus.OK);
    }

    @GetMapping
    public Iterable<InvoiceSale> findAll() {
        return invoiceRepository.findAll();
    }

    @GetMapping(path = "/bycustomername")
    public Iterable<InvoiceSale> findByCustomerName(@RequestParam
                                                        String customerName) {
        return invoiceRepository.findAllByCustomerName(customerName);
    }
}

























