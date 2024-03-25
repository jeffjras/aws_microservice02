package com.challenge.aws_microservice02.model;

import jakarta.persistence.*;

@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"code"})
        }
)
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(length = 32, nullable = false)
    private String name;

    @Column(length = 24, nullable = false)
    private String type;

    @Column(length = 8, nullable = false)
    private String code;

    private float price;

    @Column(length = 12, nullable = true)
    private String approved;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }
}
