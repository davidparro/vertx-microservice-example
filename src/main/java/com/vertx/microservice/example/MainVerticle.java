package com.vertx.microservice.example;

import com.vertx.microservice.example.handlers.ProductHandler;
import com.vertx.microservice.example.repositories.ProductsRepository;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);
    private static MongoClient mongoClient;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        super.start();

        ConfigRetriever retriever = ConfigRetriever.create(vertx);

        retriever.getConfig(config -> {
            if (config.failed()) {
                startPromise.fail(config.cause());
            } else {
                this.createMongoClient(config);
                HttpServer server = vertx.createHttpServer();
                Router mainRouter = Router.router(vertx);

                mainRouter.route("/productos/:productID").handler(ProductHandler::handleGetProduct);

                server.requestHandler(mainRouter).listen(config.result().getInteger("port"));
            }
        });
    }

    private void createMongoClient(AsyncResult<JsonObject> config) {

        String uri = config.result().getString("mongo.mongo_uri");
        String db = config.result().getString("mongo.mongo_db");
        // String user = config.result().getString("mongo.username");
        // String pass = config.result().getString("mongo.password");

        JsonObject mongoconfig = new JsonObject()
                .put("connection_string", uri)
                .put("db_name", db);
                /*.put("username", user)
                .put("password", pass);*/

        mongoClient = MongoClient.createShared(vertx, mongoconfig);

        // this.addRow();
        ProductsRepository productsRepository = new ProductsRepository();
        productsRepository.getProduct(mongoClient, "12345");
    }


}