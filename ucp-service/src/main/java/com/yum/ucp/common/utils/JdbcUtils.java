package com.yum.ucp.common.utils;



import com.yum.ucp.common.config.Global;

import java.sql.*;

/**
 * Created by admin on 2017/7/23.
 */
public class JdbcUtils {
    private static final String driver;
    private static final String url ;
    private static final String userName;
    private static final String password;

    /**
     * 装载驱动
     */
    static {

        driver = Global.getConfig("jdbc.driver2");
        url = Global.getConfig("jdbc.url2");
        userName = Global.getConfig("jdbc.username2");
        password = Global.getConfig("jdbc.password2");

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * 建立数据库连接
     *
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = null;
        conn = DriverManager.getConnection(url, userName, password);
        return conn;
    }

    public static Connection getConnection(String url) throws SQLException {
        Connection conn = null;
        conn = DriverManager.getConnection(url, userName, password);
        return conn;
    }

    /**
     * 释放连接
     * @param conn
     */
    private static void freeConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 释放statement
     * @param statement
     */
    private static void freeStatement(Statement statement) {
        try {
            statement.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 释放resultset
     * @param rs
     */
    private static void freeResultSet(ResultSet rs) {
        try {
            rs.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 释放资源
     *
     * @param conn
     * @param statement
     * @param rs
     */
    public static void free(Connection conn, Statement statement, ResultSet rs) {
        if (rs != null) {
            freeResultSet(rs);
        }
        if (statement != null) {
            freeStatement(statement);
        }
        if (conn != null) {
            freeConnection(conn);
        }
    }
}
