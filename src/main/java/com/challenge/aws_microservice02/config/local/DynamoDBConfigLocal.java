package com.challenge.aws_microservice02.config.local;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.document.Attribute;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import com.challenge.aws_microservice02.repository.ProductEventLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableDynamoDBRepositories(basePackageClasses = ProductEventLogRepository.class)
@Profile("local")
public class DynamoDBConfigLocal {

    private static final Logger LOG = LoggerFactory.getLogger(DynamoDBConfigLocal.class);

    private final AmazonDynamoDB amazonDynamoDB;

    public DynamoDBConfigLocal(AmazonDynamoDB amazonDynamoDB) {
        this.amazonDynamoDB = AmazonDynamoDBClient.builder()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration("http://localhost:4566",
                                Regions.US_EAST_1.getName()))
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();

        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
        List<AttributeDefinition> attributeDefinitionList = new ArrayList<AttributeDefinition>();
        attributeDefinitionList.add(new AttributeDefinition().withAttributeName("pk")
                .withAttributeType(ScalarAttributeType.S));
        attributeDefinitionList.add(new AttributeDefinition().withAttributeName("sk")
                .withAttributeType(ScalarAttributeType.S));

        List<KeySchemaElement> keySchemaElementList = new ArrayList<KeySchemaElement>();
        keySchemaElementList.add(new KeySchemaElement().withAttributeName("pk")
                .withKeyType(KeyType.HASH));
        keySchemaElementList.add(new KeySchemaElement().withAttributeName("sk")
                .withKeyType(KeyType.RANGE));

        CreateTableRequest createTableRequest = new CreateTableRequest()
                .withTableName("product-events")
                .withKeySchema(keySchemaElementList)
                .withAttributeDefinitions(attributeDefinitionList)
                .withBillingMode(BillingMode.PAY_PER_REQUEST);

        Table table = dynamoDB.createTable(createTableRequest);

        try {
            table.waitForActive();
        } catch (InterruptedException e) {
            LOG.error(e.getMessage());
        }
    }

    @Primary
    public DynamoDBMapperConfig dynamoDBMapperConfig() {
        return DynamoDBMapperConfig.DEFAULT;
    }

    @Bean
    @Primary
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB,
                                         DynamoDBMapperConfig config) {
        return new DynamoDBMapper(amazonDynamoDB, config);
    }

    @Bean
    @Primary
    public AmazonDynamoDB amazonDynamoDB() {
        return this.amazonDynamoDB;
    }
}
