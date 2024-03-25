package com.challenge.aws_microservice02.service;

import com.challenge.aws_microservice02.enums.EventType;
import com.challenge.aws_microservice02.model.Envelope;
import com.challenge.aws_microservice02.model.Product;
import com.challenge.aws_microservice02.model.ProductEvent;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.Topic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ProductNotifier {
    private static final Logger LOG = LoggerFactory.getLogger(
            ProductNotifier.class);
    private AmazonSNS snsClient;
    private Topic productEventsTopic;
    private ObjectMapper objectMapper;

    public ProductNotifier(AmazonSNS snsClient,
                           @Qualifier("productEventsTopic") Topic productEventsTopic,
                           ObjectMapper objectMapper) {
        this.snsClient = snsClient;
        this.productEventsTopic = productEventsTopic;
        this.objectMapper = objectMapper;
    }

    public void publishProductEvent(Product product, EventType eventType, String username) {
        ProductEvent productEvent = new ProductEvent();
        productEvent.setProductId(product.getId());
        productEvent.setCode(product.getCode());
        productEvent.setUsername(username);

        Envelope envelope = new Envelope();
        envelope.setEventType(eventType);

        try {
            envelope.setData(objectMapper.writeValueAsString(productEvent));

            snsClient.publish(
                    productEventsTopic.getTopicArn(),
                    objectMapper.writeValueAsString(envelope));

        } catch (JsonProcessingException e) {
            LOG.error("Failed to create product event message");
        }
    }
}
