package com.revpay.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBConnection {

    private static final Logger logger =
            LogManager.getLogger(DBConnection.class);

    private static final String URL =
            "jdbc:oracle:thin:@db.freesql.com:1521/23ai_34ui2";
    private static final String USER =
            "RIDDHITH04_SCHEMA_N5CLW";
    private static final String PASSWORD =
            "EJTO2$IMHO3bIE5HKTIR8UMIUBO6S8";

    public static Connection getConnection() {
        try {
            logger.info("Attempting to establish database connection");
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.info("Database connection established successfully");
            return con;

        } catch (SQLException e) {
            logger.error("Failed to establish database connection", e);
            return null;
        }
    }
}
