package ru.netology.treval;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/app";
    private static final String DB_USER = "app";
    private static final String DB_PASSWORD = "pass";

    public static boolean isPaymentStatusApproved() {
        return isStatusEquals("payment_entity", "APPROVED");
    }

    public static boolean isPaymentStatusDeclined() {
        return isStatusEquals("payment_entity", "DECLINED");
    }

    public static boolean isCreditRequestStatusApproved() {
        return isStatusEquals("credit_request_entity", "APPROVED");
    }

    public static boolean isCreditRequestStatusDeclined() {
        return isStatusEquals("credit_request_entity", "DECLINED");
    }

    private static boolean isStatusEquals(String tableName, String expectedStatus) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM " + tableName + " WHERE status = ?")) {
            statement.setString(1, expectedStatus);

            return statement.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void clearPaymentTable() {
        executeUpdate("DELETE FROM payment_entity;");
    }

    public static void clearCreditRequestTable() {
        executeUpdate("DELETE FROM credit_request_entity;");
    }

    public static void clearOrderTable() {
        executeUpdate("DELETE FROM order_entity;");
    }

    private static void executeUpdate(String query) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}