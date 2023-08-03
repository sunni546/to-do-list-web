package bezth.toDoList.database;

import bezth.toDoList.AccountIDAlreadyExistException;
import org.json.simple.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Account_SQLite {

    private final String urlDB = "jdbc:sqlite:toDoListWeb.sqlite";

    // CREATE(INSERT)
    public void insertDB(String id, String password) {
        // hash : 비밀번호 암호화
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        Connection connection = null;
        Statement stmt = null;
        try {
            // DB 연결
            connection = DriverManager.getConnection(urlDB);
            connection.setAutoCommit(false);

            stmt = connection.createStatement();
            String sql = "INSERT INTO accounts (id, password) " +
                    "VALUES ('" + id + "', '" + hashedPassword + "');";
            stmt.executeUpdate(sql);

            connection.commit();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());

            // SQL 예외 발생 후 다시 실행할 경우
            // org.sqlite.SQLiteException: [SQLITE_BUSY] The database file is locked (database is locked) 예외 수정
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException se) {
                throw new RuntimeException(se);
            }

            // ID의 Unique 관련 예외 발생
            if (e.getMessage().equals("[SQLITE_CONSTRAINT_UNIQUE] A UNIQUE constraint failed (UNIQUE constraint failed: accounts.id)")) {
                throw new AccountIDAlreadyExistException(String.format("ID[%s] is already exist", id));     // status code : 400
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException se) {
                throw new RuntimeException(se);
            }
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
                map.put("Account" + pk, account);
            }

            rs.close();
            stmt.close();
            connection.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException se) {
                throw new RuntimeException(se);
            }
        }
        JSONObject jsonObject = new JSONObject(map);
        return jsonObject;
    }

    // ID와 비밀번호를 가지고 PK 찾기
    public int findPK(String id, String password) {
        Connection connection = null;
        Statement stmt = null;
        try {
            // DB 연결
            connection = DriverManager.getConnection(urlDB);
            connection.setAutoCommit(false);

            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM accounts;");

            while (rs.next()) {
                int rs_pk = rs.getInt("pk");
                String rs_id = rs.getString("id");
                String rs_password = rs.getString("password");

                if (rs_id.equals(id)) {
                    if (BCrypt.checkpw(password, rs_password)){
                        rs.close();
                        stmt.close();
                        connection.close();
                        return rs_pk;
                    }
                }
            }

            rs.close();
            stmt.close();
            connection.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException se) {
                throw new RuntimeException(se);
            }
        }

        // ID/PW에 해당하는 사용자가 존재하지 않는 경우
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);     // status code : 401
    }
}
