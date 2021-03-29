package com.vertx.microservice.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product implements Serializable {
    private static final long serialVersionUID = 5106485512314564191L;

    // @JsonProperty(value = "id")
    private String id;

    // @JsonProperty(value = "name")
    private String name;

    // @JsonProperty(value = "description")
    private String description;
}