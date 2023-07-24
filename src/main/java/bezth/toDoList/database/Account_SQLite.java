package bezth.toDoList.database;

import bezth.toDoList.AccountIDAlreadyExistException;
import org.json.simple.JSONObject;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Account_SQLite {

    private final String urlDB = "jdbc:sqlite:toDoListWeb.sqlite";

    // CREATE(INSERT)
    public void insertDB(String id, String password) {
        Connection connection = null;
        Statement stmt = null;
        try {
            // DB 연결
            connection = DriverManager.getConnection(urlDB);
            connection.setAutoCommit(false);

            stmt = connection.createStatement();
            String sql = "INSERT INTO accounts (id, password) " +
                    "VALUES ('" + id + "', '" + password + "');";
            stmt.executeUpdate(sql);

            connection.commit();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());

            // 실행 시 SQL 에러 난 후 다시 실행할 경우
            // org.sqlite.SQLiteException: [SQLITE_BUSY] The database file is locked (database is locked) 에러 수정
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException se) {
                throw new RuntimeException(se);
            }

            //  ID의 Unique 오류
            if (e.getMessage().equals("[SQLITE_CONSTRAINT_UNIQUE] A UNIQUE constraint failed (UNIQUE constraint failed: accounts.id)")) {
                throw new AccountIDAlreadyExistException(String.format("ID[%s] is already exist", id));
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    // READ(SELECT)
    public JSONObject selectDB() {
        Map<String, Object> map = new HashMap<>();

        Connection connection = null;
        Statement stmt = null;
        try {
            // DB 연결
            connection = DriverManager.getConnection(urlDB);
            connection.setAutoCommit(false);

            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM accounts;");

            while (rs.next()) {
                Map<String, Object> account = new HashMap<>();
                int pk = rs.getInt("pk");
                account.put("pk", pk);
                String id = rs.getString("id");
                account.put("id", id);
                String password = rs.getString("password");
                account.put("password", password);
                map.put("account" + pk, account);
            }

            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException se) {
                throw new RuntimeException(se);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        JSONObject jsonObject = new JSONObject(map);
        return jsonObject;
    }

    /*
    // RESET
    public void resetDB() {
        Connection connection = null;
        Statement stmt = null;
        try {
            // DB 연결
            connection = DriverManager.getConnection(urlDB);
            connection.setAutoCommit(false);

            stmt = connection.createStatement();
            String sql = "DROP TABLE accounts;" +
                    "CREATE TABLE accounts (pk INTEGER PRIMARY KEY AUTOINCREMENT, id TEXT UNIQUE, password TEXT NOT NULL);";
            stmt.executeUpdate(sql);
            connection.commit();

            stmt.close();
            connection.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
     */
}
