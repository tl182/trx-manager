package com.trxmanager.manager.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.trxmanager.manager.app.controller.ControllerContainer;
import com.trxmanager.manager.app.controller.ExceptionMapper;
import com.trxmanager.manager.domain.DomainModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static spark.Spark.*;

@Slf4j
@RequiredArgsConstructor
public class App {

    private final int port;
    private final String connectionString;
    private final String initScriptLocation;
    private final boolean enableH2Console;

    private Connection h2InitializingConnection;
    private Server h2Server;

    private static final String H2_PARAMS = ";DB_CLOSE_DELAY=0;DB_CLOSE_ON_EXIT=FALSE;DATABASE_TO_LOWER=TRUE";

    public void init() {
        log.info("Initializing app...");

        initH2InitializingConnection();

        if (enableH2Console) {
            initH2Server();
        }

        Injector injector = Guice.createInjector(
                new DomainModule(),
                new AppModule(),
                new H2DbModule(connectionString + H2_PARAMS)
        );

        port(port);
        threadPool(10);
        injector.getInstance(ExceptionMapper.class).handleExceptions();
        injector.getInstance(ControllerContainer.class).initControllers();
        awaitInitialization();
    }

    public void shutDown() {
        stop();
        awaitStop();

        stopH2Server();
        closeH2InitializingConnection();

        log.info("App has been shut down");
    }

    private void initH2InitializingConnection() {
        try {
            Class.forName("org.h2.Driver");
            String initializingConnectionString =
                    connectionString + H2_PARAMS + ";INIT=RUNSCRIPT FROM '" + initScriptLocation + "'";
            h2InitializingConnection = DriverManager.getConnection(initializingConnectionString);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeH2InitializingConnection() {
        try {
            h2InitializingConnection.createStatement().execute("SHUTDOWN");
            h2InitializingConnection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initH2Server() {
        try {
            h2Server = Server.createWebServer("-webPort", "9090").start();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void stopH2Server() {
        if (h2Server != null) {
            h2Server.stop();
        }
    }
}
