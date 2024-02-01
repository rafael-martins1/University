package es.softtek.jwtDemo.config;

import java.sql.*;

public class PostgresConnector {

    private final String PG_HOST;
    private final String PG_DB;
    private final String USER;
    private final String PWD;

    Connection con = null;
    Statement stmt = null;

    public PostgresConnector(String host, String db, String user, String pw) {
        PG_HOST=host;
        PG_DB= db;
        USER=user;
        PWD= pw;
    }
    public void connect() {
        try {
            Class.forName("org.postgresql.Driver");

            con = DriverManager.getConnection("jdbc:postgresql://" + PG_HOST + ":5433/" + PG_DB, USER, PWD);

            stmt = con.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Problems setting the connection");
        }
    }
    public void disconnect() {
        try {
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return con.prepareStatement(sql);
    }

}