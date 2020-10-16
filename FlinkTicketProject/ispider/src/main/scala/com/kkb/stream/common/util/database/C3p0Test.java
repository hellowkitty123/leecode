package com.kkb.stream.common.util.database;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;
public class C3p0Test {
    private static Connection conn;
    private static ComboPooledDataSource ds = new ComboPooledDataSource();

    public static synchronized Connection getConnection() {
        try {
            ds.setDriverClass("com.mysql.jdbc.Driver");
            ds.setJdbcUrl("jdbc:mysql://hadoop004:3306/gciantispider?useUnicode=true&characterEncoding=UTF8&useServerPrepStmts=true&prepStmtCacheSqlLimit=256&cachePrepStmts=true&prepStmtCacheSize=256&rewriteBatchedStatements=true");
            ds.setUser("root");
            ds.setPassword("I8i6XhKUik");
            conn = ds.getConnection();

        } catch (SQLException | PropertyVetoException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
