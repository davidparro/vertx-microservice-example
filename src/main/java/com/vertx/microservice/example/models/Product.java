package com.vertx.microservice.example.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class Product implements Serializable {
    private static final long serialVersionUID = 5106485512314564191L;

    private String id;

    private String name;

    private String description;
}