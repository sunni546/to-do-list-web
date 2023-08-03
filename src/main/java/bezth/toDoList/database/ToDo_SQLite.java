package bezth.toDoList.database;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ToDo_SQLite {

    private final String urlDB = "jdbc:sqlite:toDoListWeb.sqlite";

    // CREATE(INSERT)
    public void insertDB(String title, String content, int account_id) {
        Connection connection = null;
        Statement stmt = null;
        try {
            // DB 연결
            connection = DriverManager.getConnection(urlDB);
            connection.setAutoCommit(false);

            stmt = connection.createStatement();
            String sql = "INSERT INTO todolist (title, content, done, account_id) " +
                    "VALUES ('" + title + "', '" + content + "', 0, " + account_id + ");";
            stmt.executeUpdate(sql);

            stmt.close();
            connection.commit();
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
    }

    // READ(SELECT)
    public JSONObject selectDB(int account_id) {
        Map<String, Object> map = new HashMap<>();

        Connection connection = null;
        Statement stmt = null;
        try {
            // DB 연결
            connection = DriverManager.getConnection(urlDB);
            connection.setAutoCommit(false);

            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM todolist WHERE account_id = " + account_id + ";");

            while (rs.next()) {
                Map<String, Object> toDo = new HashMap<>();
                int id = rs.getInt("id");
                toDo.put("id", id);
                String title = rs.getString("title");
                toDo.put("title", title);
                String content = rs.getString("content");
                toDo.put("content", content);
                Boolean done = rs.getBoolean("done");
                toDo.put("done", done);
                toDo.put("account_id", account_id);
                map.put("toDo" + id, toDo);
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

    // UPDATE
    public void updateDB(int id, Optional<Object> title, Optional<Object> content, Optional<Object> done, int account_id) {
        Connection connection = null;
        Statement stmt = null;
        try {
            // DB 연결
            connection = DriverManager.getConnection(urlDB);
            connection.setAutoCommit(false);

            stmt = connection.createStatement();

            // 수정할 '할 일'의 account_id 구하기
            ResultSet rs = stmt.executeQuery("SELECT account_id FROM todolist WHERE ID = " + id + ";");
            int rs_account_id = rs.getInt("account_id");
            rs.close();

            // 수정할 '할 일'이 존재하지 않는 경우
            if (rs_account_id == 0) {
                stmt.close();
                connection.close();
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);    // status code : 404
            }

            // 수정할 '할 일'의 account_id(rs_account_id)와 요청받은 계정의 pk(account_id)가 같은 경우
            if (rs_account_id == account_id) {
                String sql = "UPDATE todolist SET ";
                if (title != null) {
                    sql += "title = '" + title.get() + "',";
                } if (content != null) {
                    sql += "content = '" + content.get() + "',";
                } if (done != null) {
                    sql += "done = " + ((Boolean) done.get()? 1 : 0) + ",";

                } sql = sql.substring(0, sql.length() - 1);     // 마지막 "," 제거
                sql += " WHERE ID = " + id + ";";

                stmt.executeUpdate(sql);
                connection.commit();
            } else {    // 사용자에게 권한이 없는 경우
                stmt.close();
                connection.close();
                // throw new ResponseStatusException(HttpStatus.FORBIDDEN);    // status code : 403
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);    // status code : 404
            }

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
            throw new RuntimeException(e);
        }
    }

    // DELETE
    public void deleteDB(int id, int account_id) {
        Connection connection = null;
        Statement stmt = null;
        try {
            // DB 연결
            connection = DriverManager.getConnection(urlDB);
            connection.setAutoCommit(false);

            stmt = connection.createStatement();

            // 삭제할 '할 일'의 account_id 구하기
            ResultSet rs = stmt.executeQuery("SELECT account_id FROM todolist WHERE ID = " + id + ";");
            int rs_account_id = rs.getInt("account_id");
            rs.close();

            // 삭제할 '할 일'이 존재하지 않는 경우
            if (rs_account_id == 0) {
                stmt.close();
                connection.close();
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);    // status code : 404
            }

            // 삭제할 '할 일'의 account_id(rs_account_id)와 요청받은 계정의 pk(account_id)가 같은 경우
            if (rs_account_id == account_id) {
                String sql = "DELETE FROM todolist WHERE ID = " + id + ";";
                stmt.executeUpdate(sql);
                connection.commit();
            } else {    // 사용자에게 권한이 없는 경우
                stmt.close();
                connection.close();
                // throw new ResponseStatusException(HttpStatus.FORBIDDEN);    // status code : 403
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);    // status code : 404
            }

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
            throw new RuntimeException(e);
        }
    }

    // READ(SELECT)
    public JSONObject selectAllDB() {
        Map<String, Object> map = new HashMap<>();

        Connection connection = null;
        Statement stmt = null;
        try {
            // DB 연결
            connection = DriverManager.getConnection(urlDB);
            connection.setAutoCommit(false);

            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM todolist;");

            while (rs.next()) {
                Map<String, Object> toDo = new HashMap<>();
                int id = rs.getInt("id");
                toDo.put("id", id);
                String title = rs.getString("title");
                toDo.put("title", title);
                String content = rs.getString("content");
                toDo.put("content", content);
                Boolean done = rs.getBoolean("done");
                toDo.put("done", done);
                int account_id = rs.getInt("account_id");
                toDo.put("account_id", account_id);
                map.put("toDo" + id, toDo);
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
}