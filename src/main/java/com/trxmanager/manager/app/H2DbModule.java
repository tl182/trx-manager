package com.trxmanager.manager.app;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.h2.jdbcx.JdbcConnectionPool;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DataSourceConnectionProvider;

import javax.sql.DataSource;

@RequiredArgsConstructor
public class H2DbModule extends AbstractModule {

    private final String connectionString;

    @Provides
    @Singleton
    public DataSource dataSource() {
        return JdbcConnectionPool.create(connectionString, "", "");
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
