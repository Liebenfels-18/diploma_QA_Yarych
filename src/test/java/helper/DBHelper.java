package helper;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DBHelper {
    private static QueryRunner request;
    private static Connection conn;
    private static final String url = System.getProperty("urlDB");
    private static final String user = System.getProperty("userDB");
    private static final String password = System.getProperty("passwordDB");


    public static String getDataFromDB(String query) throws SQLException {
        String result = "";
        var runner = new QueryRunner();
        try
                (var conn = DriverManager.getConnection(url, user, password)) {

            result = runner.query(conn, query, new ScalarHandler<String>());
            //System.out.println(result);
            return result;
        }
    }

    @SneakyThrows
    public static String getCreditID() throws SQLException {
        var creditId = "SELECT credit_id FROM order_entity ORDER BY created DESC limit 1";
        return getDataFromDB(creditId);
    }

    @SneakyThrows
    public static String getPaymentID() throws SQLException {
        var paymentId = "SELECT payment_id FROM order_entity ORDER BY created DESC limit 1";
        return getDataFromDB(paymentId);
    }


    @SneakyThrows
    public static void runSQL() {
        request = new QueryRunner();
        conn = DriverManager.getConnection(url, user, password);
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

}