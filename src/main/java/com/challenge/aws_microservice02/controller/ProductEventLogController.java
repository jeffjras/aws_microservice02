package com.challenge.aws_microservice02.controller;

import com.challenge.aws_microservice02.model.ProductEventLogDTO;
import com.challenge.aws_microservice02.repository.ProductEventLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api")
public class ProductEventLogController {

    private ProductEventLogRepository productEventLogRepository;

    @Autowired
    public ProductEventLogController(ProductEventLogRepository productEventLogRepository) {
        this.productEventLogRepository = productEventLogRepository;
    }

    @GetMapping("/events")
    public List<ProductEventLogDTO> getAllEvents() {
        return StreamSupport
                .stream(productEventLogRepository.findAll().spliterator(), false)
                .map(ProductEventLogDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/events/{code}")
    public List<ProductEventLogDTO> findByCode(@PathVariable String code) {
        return productEventLogRepository.findAllByPk(code)
                .stream()
                .map(ProductEventLogDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/events/{code}/{event}")
    public List<ProductEventLogDTO> findByCodeAndEventType(@PathVariable String code,
                                                           @PathVariable String event) {
        return productEventLogRepository.findAllByPkAndSkStartsWith(code, event)
                .stream()
                .map(ProductEventLogDTO::new)
                .collect(Collectors.toList());
    }
}

















