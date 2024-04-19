package helper;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DBHelper {
    private static QueryRunner request;
    private static Connection conn;
    private static final String url = System.getProperty("urlDB");
    private static final String user = System.getProperty("userDB");
    private static final String password = System.getProperty("passwordDB");

    @SneakyThrows
    public static void runSQL() {
        request = new QueryRunner();
        conn = DriverManager.getConnection(url, user, password);
    }

    @SneakyThrows
    public static String getCreditID() {
        runSQL();
        var data = "SELECT credit_id FROM order_entity ORDER BY created DESC limit 1";
        return request.query(conn, data, new ScalarHandler<>());
    }

    @SneakyThrows
    public static String getPaymentID() {
        runSQL();
        var data = "SELECT payment_id FROM order_entity ORDER BY created DESC limit 1";
        return request.query(conn, data, new ScalarHandler<>());
    }

    @SneakyThrows
    public static String getCreditStatus(String paymentId) {
        runSQL();
        var data = "SELECT status FROM credit_request_entity WHERE bank_id = ?";
        return request.query(conn, data, new ScalarHandler<>(), paymentId);
    }

    @SneakyThrows
    public static String getStatus(String paymentId) {
        runSQL();
        var data = "SELECT status FROM payment_entity WHERE transaction_id = ?";
        return request.query(conn, data, new ScalarHandler<>(), paymentId);
    }


    public static void checkStatusPaymentApproved() {
        String id = DBHelper.getPaymentID();
        String actual = DBHelper.getStatus(id);
        String expected = "APPROVED";
        assertEquals(expected, actual);
    }

    public static void checkStatusPaymentDeclined() {
        String id = DBHelper.getPaymentID();
        String actual = DBHelper.getStatus(id);
        String expected = "DECLINED";
        assertEquals(expected, actual);
    }

    public static void checkStatusCreditApproved() {
        String id = DBHelper.getCreditID();
        String actual = DBHelper.getCreditStatus(id);
        String expected = "APPROVED";
        assertEquals(expected, actual);
    }

    public static void checkStatusCreditDeclined() {
        String id = DBHelper.getCreditID();
        String actual = DBHelper.getCreditStatus(id);
        String expected = "DECLINED";
        assertEquals(expected, actual);
    }
}