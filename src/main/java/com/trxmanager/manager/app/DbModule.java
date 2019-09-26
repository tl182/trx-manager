package com.trxmanager.manager.app;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.h2.jdbcx.JdbcConnectionPool;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DataSourceConnectionProvider;

import javax.sql.DataSource;

public class DbModule extends AbstractModule {

    public static final String CONNECTION_STRING =
            "jdbc:h2:mem:trx_manager;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=TRUE;DATABASE_TO_LOWER=TRUE";

    private static final Provider<DataSource> DATA_SOURCE_PROVIDER =
            () -> JdbcConnectionPool.create(CONNECTION_STRING, "", "");

    @Override
    protected void configure() {
        bind(DataSource.class).toProvider(DATA_SOURCE_PROVIDER).in(Singleton.class);
    }

    @Provides
    @Singleton
    public ConnectionProvider connectionProvider(DataSource dataSource) {
        return new DataSourceConnectionProvider(dataSource);
    }

    @Provides
    @Singleton
    public DSLContext dslContext(ConnectionProvider connectionProvider) {
        return DSL.using(connectionProvider, SQLDialect.H2);
    }
}
