package dk.madplanen.persistence;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;

class Database {

    private static final PGSimpleDataSource datasource;

    static {
        int portNumber = Integer.parseInt(System.getenv("DB_PORT"));
        String serverName = System.getenv("DB_HOST");

        PGSimpleDataSource simpleSource = new PGSimpleDataSource();
        simpleSource.setServerNames(new String[] {serverName});
        simpleSource.setDatabaseName(System.getenv("DB_NAME"));
        simpleSource.setUser(System.getenv("DB_USER"));
        simpleSource.setPassword(System.getenv("DB_PASSWORD"));
        simpleSource.setPortNumbers(new int[] {portNumber});
        simpleSource.setCurrentSchema(System.getenv("DB_SCHEMA"));

        datasource = simpleSource;
    }

    private Database() {}

    public static Connection getConnection() throws SQLException {
        return datasource.getConnection();
    }
}
