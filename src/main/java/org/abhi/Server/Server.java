package org.abhi.Server;

import io.grpc.ServerBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);

    private io.grpc.Server server;
    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try {
            server = ServerBuilder.forPort(port)
                    // TODO
//                    .intercept(new ExceptionInterceptor())
//                    .addService(new ParametersServiceImpl())
//                    .addService(new JobsServiceImpl())
//                    .addService(new WatchdogServiceImpl())
                    .build();
            server.start();

            logger.info("Server Started at port :" + port);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Server shutting down");
                try {
                    this.stop();
                } catch (InterruptedException e) {
                    logger.error("Server shutdown interrupted", e);
                }
            }));

        } catch (Exception e) {
            logger.error("Server failed to start:", e);
        }
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.awaitTermination(1, TimeUnit.SECONDS);
            logger.info("Server shutdown");
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}
