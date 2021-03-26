package com.vertx.microservice.example;

import com.vertx.microservice.example.handlers.ProductHandler;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        super.start();

        ConfigRetriever retriever = ConfigRetriever.create(vertx);

        retriever.getConfig(json -> {
            this.createMongoClient();
            HttpServer server = vertx.createHttpServer();
            Router mainRouter = Router.router(vertx);

            mainRouter.route("/ruta/:productID").handler(ProductHandler::handleGetProduct);

            server.requestHandler(mainRouter).listen(8080);
        });

        /*

        discovery = ServiceDiscovery.create(vertx,
                new ServiceDiscoveryOptions()
                        .setAnnounceAddress("service-announce")
                        .setName("hello-world-emitter"));

        discovery.publish(HttpEndpoint.createRecord(
                "hello-world-service",
                "localhost", 8888,
                "/hello-world"),
                ar -> {
                    if (ar.succeeded()) {
                        System.out.println("REST API published" + ar.toString());
                    } else {
                        System.out.println("Unable to publish the REST API: " +
                                ar.cause().getMessage());
                    }
                });

        HttpServer server = vertx.createHttpServer().requestHandler(req -> {
            req.response()
                    .putHeader("content-type", "text/plain")
                    .end("Hello from Vert.x!");
        }).requestHandler(router).listen(8888, http -> {
            if (http.succeeded()) {
                startPromise.complete();
                System.out.println("HTTP server started on port 8888");
            } else {
                startPromise.fail(http.cause());
            }
        });*/
    }

    private void createMongoClient() {
        JsonObject config = Vertx.currentContext().config();

        String uri = config.getString("mongo_uri");
        System.out.println("MONGO URI: " + uri);
        if (uri == null) {
            uri = "mongodb://localhost:27017";
        }
        String db = config.getString("mongo_db");
        System.out.println("MONGO DB: " + db);
        if (db == null) {
            db = "test";
        }

        JsonObject mongoconfig = new JsonObject()
                .put("connection_string", uri)
                .put("db_name", db);

        MongoClient mongo = MongoClient.createShared(vertx, mongoconfig);

        this.addRow(mongo);
    }

    private void addRow(MongoClient mongoClient) {
        JsonObject product1 = new JsonObject().put("id", "12345").put("name", "Cooler").put("description", "100.0");

        mongoClient.save("products", product1, id -> {
            System.out.println("Inserted id: " + id.result());

            /*mongoClient.find("products", new JsonObject().put("id", "12345"), res -> {
                System.out.println("Name is " + res.result().get(0).getString("name"));

                mongoClient.remove("products", new JsonObject().put("id", "12345"), rs -> {
                    if (rs.succeeded()) {
                        System.out.println("Product removed ");
                    }
                });

            });*/

        });
    }
}
