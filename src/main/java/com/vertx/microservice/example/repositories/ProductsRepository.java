package com.vertx.microservice.example.repositories;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class ProductsRepository {

    public void getProduct(MongoClient mongoClient, String id) {

        mongoClient.find("products", new JsonObject().put("id", id), res -> {
            if (res.succeeded()) {

            } else {
                res.cause().printStackTrace();
            }
        });
    }

    public JsonObject get(String id) {
        return new JsonObject().put("id", id);
    }
}
