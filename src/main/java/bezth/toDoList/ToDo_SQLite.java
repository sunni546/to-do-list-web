package bezth.toDoList;

import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ToDo_SQLite {
    private final String urlDB = "jdbc:sqlite:toDoListWeb.sqlite";

    // CREATE(INSERT)
    public void insertDB(String title, String content) {
        Connection connection = null;
        Statement stmt = null;
        try {
            // DB 연결
            connection = DriverManager.getConnection(urlDB);
            connection.setAutoCommit(false);

            stmt = connection.createStatement();
            String sql = "INSERT INTO todolist (title, content, done) " +
                    "VALUES ('" + title + "', '" + content + "', 0);";
            stmt.executeUpdate(sql);

            stmt.close();
            connection.commit();
            connection.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
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
                map.put("toDO" + id, toDo);
            }

            rs.close();
            stmt.close();
            connection.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        JSONObject jsonObject = new JSONObject(map);
        return jsonObject;
    }

    // UPDATE
    public void updateDB(int id, Optional<Object> title, Optional<Object> content, Optional<Object> done) {
        Connection connection = null;
        Statement stmt = null;
        try {
            // DB 연결
            connection = DriverManager.getConnection(urlDB);
            connection.setAutoCommit(false);

            stmt = connection.createStatement();

            String sql = "UPDATE todolist set ";
            if (title != null) {
                sql += "title = '" + title.get() + "',";
            } if (content != null) {
                sql += "content = '" + content.get() + "',";
            } if (done != null) {
                sql += "done = " + ((Boolean) done.get()? 1 : 0) + ",";

            } sql = sql.substring(0, sql.length() - 1);     // 마지막 "," 제거
            sql += " where ID = " + id + ";";

            stmt.executeUpdate(sql);
            connection.commit();

            stmt.close();
            connection.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    // DELETE
    public void deleteDB(int id) {
        Connection connection = null;
        Statement stmt = null;
        try {
            // DB 연결
            connection = DriverManager.getConnection(urlDB);
            connection.setAutoCommit(false);

            stmt = connection.createStatement();
            String sql = "DELETE from todolist where ID = " + id + ";";
            stmt.executeUpdate(sql);
            connection.commit();

            stmt.close();
            connection.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }
}