package com.trxmanager.manager;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.trxmanager.manager.app.AppConfig;
import com.trxmanager.manager.app.AppModule;
import com.trxmanager.manager.app.DbModule;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static spark.Spark.*;

@Slf4j
public class Main {

    private static final int PORT = 8080;
    private static final String INITIALIZING_CONNECTION_STRING =
            DbModule.CONNECTION_STRING + ";INIT=RUNSCRIPT FROM 'classpath:db/migration/init.sql'";

    public static void main(String[] args) {
        runServer(PORT, INITIALIZING_CONNECTION_STRING, true);
    }

    public static void runServer(int port, String initializingConnectionString, boolean enableH2Console) {
        Connection initializingConnection = null;
        Server h2Server = null;

        try {
            Class.forName("org.h2.Driver");

            // This connection initializes h2 with init.sql script,
            // it should be kept alive until the end of app lifecycle
            initializingConnection = DriverManager.getConnection(initializingConnectionString);

            if (enableH2Console) {
                h2Server = Server.createWebServer("-webPort", "9090").start();
            }

            // Launching app
            Injector injector = Guice.createInjector(new AppModule(), new DbModule());
            injector.getInstance(AppConfig.class).init(port);

            awaitInitialization();
        } catch (Exception e) {
            log.error("Caught unexpected exception, exiting...", e);

            if (h2Server != null) {
                h2Server.stop();
            }

            if (initializingConnection != null) {
                try {
                    initializingConnection.close();
                } catch (SQLException ex) {
                    log.error("Error while closing initializingConnection", ex);
                }
            }

            stop();
            awaitStop();
            System.exit(1);
        }
    }
}
