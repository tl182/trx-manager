package com.trxmanager.manager;

import com.trxmanager.manager.app.App;

public class Main {

    private static final int PORT = 8080;
    private static final String CONNECTION_STRING = "jdbc:h2:mem:trx_manager";
    private static final String INIT_SCRIPT_LOCATION = "classpath:db/migration/init.sql";
    private static final boolean ENABLE_H2_CONSOLE = true;

    public static void main(String[] args) {
        App app = new App(PORT, CONNECTION_STRING, INIT_SCRIPT_LOCATION, ENABLE_H2_CONSOLE);
        app.init();
        Runtime.getRuntime().addShutdownHook(new Thread(app::shutDown));
    }
}
