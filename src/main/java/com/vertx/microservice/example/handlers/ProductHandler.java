package com.vertx.microservice.example.handlers;

import com.vertx.microservice.example.repositories.ProductsRepository;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class ProductHandler {
    public static ProductsRepository productsRepository;

    public ProductHandler(ProductsRepository productsRepository) {
        ProductHandler.productsRepository = productsRepository;
    }

    public static void handleGetProduct(RoutingContext routingContext) {
        String productID = routingContext.request().getParam("productID");
        HttpServerResponse response = routingContext.response();
        if (productID == null) {
            sendError(400, response);
        } else {
            JsonObject product = productsRepository.get(productID);
            if (product == null) {
                sendError(404, response);
            } else {
                response.putHeader("content-type", "application/json").end(product.encodePrettily());
            }
        }
    }

    private static void sendError(int statusCode, HttpServerResponse response) {
        response.setStatusCode(statusCode).end();
    }
}
