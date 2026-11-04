package com.redpulse.util;

import java.sql.Connection;

public class DBTest {

    public static void main(String[] args) {

        try {
            Connection connection = DBConnection.getConnection();

            if (connection != null) {
                System.out.println("MYSQL CONNECTION SUCCESSFUL!");
                connection.close();
            }

        } catch (Exception e) {
            System.out.println("MYSQL CONNECTION FAILED!");
            e.printStackTrace();
        }
    }
}
