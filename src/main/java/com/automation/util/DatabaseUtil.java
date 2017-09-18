package com.automation.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class DatabaseUtil {

    private static final Logger LOGGER = Logger.getLogger(DatabaseUtil.class);

    private Connection dbConnection;

    public boolean checkSelectSQL(String sql) {
        if (StringUtils.isEmpty(sql)) {
            // no need to validate sql
            return true;
        }

        boolean result = false;
        Statement statement = null;
        try {
            statement = dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.last();
            if (resultSet.getRow() > 0) {
                result = true;
            }

        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                LOGGER.error(e);
            }
        }

        return result;

    }

    public void deleteSQL(String sql) throws Exception {
        if (StringUtils.isEmpty(sql)) {
            return;
        }
        Statement statement = dbConnection.createStatement();
        statement.executeQuery(sql);
    }

    public void openDBConnection(String driver, String connectionString, String user, String password) {
        try {
            Class.forName(driver);
            dbConnection = DriverManager.getConnection(connectionString, user, password);
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    public void closeDBConnection() {
        try {
            if (dbConnection != null) {
                dbConnection.close();
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

}
